import java.io.*;
import java.util.*;
import java.net.*;

public class Chatserver {

	private static final int PORT=9234;
	private static HashSet<PrintWriter>writers=new HashSet<>();
	private static HashSet<String> arrNames=new HashSet<>();

	public static void main(String[] args) throws Exception{

		System.out.println("The server is ready.");
		ServerSocket ss=new ServerSocket(PORT);
		try
		{
			while(true)
			{
				new ClientThread(ss.accept()).start();

			}
		}
		finally
		{
			ss.close();
		}


	}
	private static class ClientThread extends Thread
	{
		private String name;
		private Socket skt;
		private PrintWriter pw;
		private BufferedReader br;

		public ClientThread(Socket s)
		{
			skt=s;

		}
		public void run()
		{
			try
			{
				br=new BufferedReader(new InputStreamReader(skt.getInputStream()));
				pw=new PrintWriter(skt.getOutputStream(),true);
				while(true)
				{
					pw.println("SUBMITNAME");
					name=br.readLine();
					synchronized(arrNames)
					{
						if(!arrNames.contains(name))
						{	
							arrNames.add(name);
							break;
						}

					}
				}
				pw.println("ACCEPTED");
				writers.add(pw);
				while(true)
				{
					String input=br.readLine();
					if(input==null)
						return;

					for(PrintWriter p:writers)
					{
						p.println("MESSAGE "+name+": "+input);
					}
				}

			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());

			}
			finally
			{
				if(name!=null)
					arrNames.remove(name);

				if(pw!=null)
					writers.remove(pw);
				try {
					skt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
