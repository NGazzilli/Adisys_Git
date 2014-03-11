package adisys.server.strumenti;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * Classe che si occupa della riproduzione dei suoni del sistema
 * 
*/
public class Sound {
	
	
	/**
	 * Riproduce il suono d'avvio
	*/
	public static void riproduciAvvio(){
		
		riproduciFileAudio("sounds/avvio.wav");
		
	}
	
	
	
	/**
	 * Riproduce il suono di errore
	*/
	public static void riproduciErrorSound(){
		
		adisys.server.strumenti.Sound.riproduciFileAudio("sounds/sonar.wav");
		
	}
	
	
	
	/**
	 * Riproduce un file audio nel formato .wav
	 * @param pathName il percorso del file audio .wav da eseguire
	*/
	private static void riproduciFileAudio(String pathName){
		
		File sf=new File(pathName);
		
		try {
			
			AudioFileFormat aff=AudioSystem.getAudioFileFormat(sf);
			AudioInputStream ais=AudioSystem.getAudioInputStream(sf);
			
			AudioFormat af=aff.getFormat();
			
			DataLine.Info info = new DataLine.Info(
					Clip.class,ais.getFormat(),
					((int) ais.getFrameLength() *af.getFrameSize())
			);
			
			Clip ol = (Clip) AudioSystem.getLine(info);
			ol.open(ais);
			ol.start();
					
		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
			System.err.println("Sound.riproduciFileAudio : Tipo di file audio non supportato");
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("Sound.riproduciFileAudio : Errore nella lettura del file audio");
		} catch (LineUnavailableException e) {
//			e.printStackTrace();
			System.err.println("Sound.riproduciFileAudio : Formato del file audio non riproducibile su questo computer");
		}
		
	}
	
}
