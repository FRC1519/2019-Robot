import java.io.File;
    import java.io.IOException;
    import java.net.MalformedURLException;
    import javax.sound.sampled.AudioInputStream;
    import javax.sound.sampled.AudioSystem;
    import javax.sound.sampled.Clip;
    import javax.sound.sampled.LineUnavailableException;
    import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound extends Thread
{
	String m_file;

	public PlaySound(String file)
	{
		m_file = file;
	}
	
	public static void main(String args[])
	{
		System.out.println("Hello, World"); 
		new PlaySound("bugs_answer.wav").start();
		new PlaySound("ltef_002.wav").start();
		System.out.println("Hello, World"); 
	}

	public void run()
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(this.m_file)));
			clip.start();
			if( !clip.isRunning())
			{
				while(!clip.isRunning())
				{
					Thread.sleep(500);
					System.out.println("not playing "+this.m_file);
				}
			}
			while(clip.isRunning())
			{
				Thread.sleep(500);
				System.out.println("playing "+this.m_file);
			}
			System.out.println("end of "+this.m_file); 
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}
}