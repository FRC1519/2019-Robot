import java.io.File;
    import java.io.IOException;
    import java.net.MalformedURLException;
    import javax.sound.sampled.AudioInputStream;
    import javax.sound.sampled.AudioSystem;
    import javax.sound.sampled.Clip;
    import javax.sound.sampled.LineUnavailableException;
    import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound
{
	public static void main(String args[])
	{
		System.out.println("Hello, World"); 
		play("bugs_answer.wav");
		System.out.println("Hello, World"); 
	}

	public static void play(String filename)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.start();
			while(!clip.isRunning());
			while(clip.isRunning())
			{
			Thread.sleep(10);
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}
}