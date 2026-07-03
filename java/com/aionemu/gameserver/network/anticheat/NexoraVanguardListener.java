package com.aionemu.gameserver.network.anticheat;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexoraVanguardListener
{
	
	private static final Logger log = LoggerFactory.getLogger(NexoraVanguardListener.class);
	
	private static final int DEFAULT_PORT = 5005;
	private static final int BACKLOG = 50;
	private static final int READ_TIMEOUT_MS = 2500;
	private static final int ACCEPT_TIMEOUT_MS = 1000;
	
	private static NexoraVanguardListener instance;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
		Thread t = new Thread(r, "NexoraVanguard-AC");
		t.setDaemon(true);
		return t;
	});
	
	private ServerSocketChannel serverChannel;
	private Selector selector;
	
	public static NexoraVanguardListener getInstance()
	{
		if (instance == null)
		{
			instance = new NexoraVanguardListener();
		}
		return instance;
	}
	
	private NexoraVanguardListener()
	{
	}
	
	public void start()
	{
		if (running.compareAndSet(false, true))
		{
			int port = GSConfig.NEXORAVANGUARD_PORT > 0 ? GSConfig.NEXORAVANGUARD_PORT : DEFAULT_PORT;
			log.info("NexoraVanguard AntiCheat listener starting on port {}...", port);
			
			executor.execute(() -> {
				try
				{
					serverChannel = ServerSocketChannel.open();
					serverChannel.configureBlocking(false);
					serverChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port), BACKLOG);
					
					selector = Selector.open();
					serverChannel.register(selector, SelectionKey.OP_ACCEPT);
					log.info("NexoraVanguard AntiCheat listener ready on port {}", port);
					
					while (running.get())
					{
						selector.select(ACCEPT_TIMEOUT_MS);
						Set<SelectionKey> keys = selector.selectedKeys();
						Iterator<SelectionKey> it = keys.iterator();
						while (it.hasNext())
						{
							SelectionKey key = it.next();
							it.remove();
							if (key.isValid() && key.isAcceptable())
							{
								accept(key);
							}
						}
					}
				}
				catch (Exception e)
				{
					log.error("NexoraVanguard listener error", e);
				}
				finally
				{
					shutdown();
				}
			});
		}
	}
	
	public void stop()
	{
		if (running.compareAndSet(true, false))
		{
			if (selector != null && selector.isOpen())
			{
				selector.wakeup();
			}
			executor.shutdown();
			shutdown();
			log.info("NexoraVanguard AntiCheat listener stopped.");
		}
	}
	
	private void shutdown()
	{
		try
		{
			if (selector != null && selector.isOpen())
			{
				selector.close();
			}
		}
		catch (Exception ignore)
		{
		}
		try
		{
			if (serverChannel != null && serverChannel.isOpen())
			{
				serverChannel.close();
			}
		}
		catch (Exception ignore)
		{
		}
	}
	
	private void accept(SelectionKey key)
	{
		try
		{
			SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
			if (client == null)
			{
				return;
			}
			client.configureBlocking(true);
			client.socket().setSoTimeout(READ_TIMEOUT_MS);
			
			ThreadPoolManager.getInstance().execute(() -> handleClient(client));
		}
		catch (Exception e)
		{
			log.warn("NexoraVanguard accept error: {}", e.getMessage());
		}
	}
	
	private void handleClient(SocketChannel client)
	{
		String src = null;
		try (java.net.Socket s = client.socket())
		{
			src = s.getRemoteSocketAddress().toString();
			ByteBuffer header = ByteBuffer.allocate(4);
			readExact(client, header);
			int len = header.getInt(0);
			if (len <= 0 || len > 8192)
			{
				log.warn("[NEXORA] Invalid payload size={} from={}", len, src);
				return;
			}
			ByteBuffer payload = ByteBuffer.allocate(len);
			readExact(client, payload);
			parsePayload(src, payload.array());
		}
		catch (SocketTimeoutException e)
		{
		}
		catch (Exception e)
		{
			log.warn("[NEXORA] I/O error from {}", src, e);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (Exception ignore)
			{
			}
		}
	}
	
	public void readExact(SocketChannel ch, ByteBuffer buf) throws Exception
	{
		while (buf.hasRemaining())
		{
			int r = ch.read(buf);
			if (r < 0)
			{
				throw new Exception("stream closed");
			}
		}
	}
	
	private void parsePayload(String src, byte[] data)
	{
		if (data.length < 16)
		{
			return;
		}
		java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(data));
		try
		{
			int type = dis.readInt();
			int pid = dis.readInt();
			int timestamp = dis.readInt();
			String moduleName = dis.readUTF();
			int extraLen = Byte.toUnsignedInt(dis.readByte());
			byte[] extraArr = new byte[extraLen];
			dis.readFully(extraArr);
			String extra = new String(extraArr, java.nio.charset.StandardCharsets.UTF_8);
			handleReport(src, type, pid, timestamp, moduleName, extra);
		}
		catch (Exception e)
		{
			log.warn("[NEXORA] Malformed packet from {}", src, e);
		}
	}
	
	public void handleReport(String src, int type, int pid, int timestamp, String moduleName, String extra)
	{
		String line = "[NEXORA][type=" + type + "] pid=" + pid + " ts=" + timestamp + " module=" + moduleName + " extra=" + extra + " src=" + src;
		switch (type)
		{
			case 1 -> log.warn("[NEXORA][INJECTION-CRITICAL] {}", line);
			case 2 -> log.warn("[NEXORA][SUSPICIOUS] {}", line);
			case 3 -> log.warn("[NEXORA][SPEED-HACK-SUSPECT] {}", line);
			case 4 -> log.debug("[NEXORA][HEARTBEAT] {}", line);
			default -> log.warn("[NEXORA][UNKNOWN-TYPE] {}", line);
		}
	}
}
