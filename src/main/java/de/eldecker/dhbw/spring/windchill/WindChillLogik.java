package de.eldecker.dhbw.spring.windchill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Diese Bean-Klasse enthält eine Methode zur Berechnung der gefühlten Temperatur
 * bei kalten Temperaturen (unter 10 Grad Celsius).
 */
@Component
public class WindChillLogik {
	
	private static Logger LOG = LoggerFactory.getLogger( WindChillRestController.class );

	
	/** 
	 * Berechnung der gefühlten Temperatur nach
	 * <a href="https://de.wikipedia.org/wiki/Windchill#Aktuelle_Berechnung_und_Tabelle">Wikipedia</a>.
	 * 
	 * @param physTemperatur Tatsächliche (physische) Temperatur in Grad Celsius, muss im  
	 *                       Bereich -50.0 Grad und 10.0 Grad liegen
	 * 
	 * @param windgeschwindigkeit Windgeschwindigkeit in km/h; muss innerhalb 5.0 km/h
	 *                            und 60 km/h sein
	 * 
	 * @return Gefühlte Temperatur
	 * 
	 * @throws WindChillException Mindestens einer der beiden Eingabewerte liegt außerhalb
	 *                            seines zulässigen Bereichs 
	 */
	public double berechneWindChillTemperatur( double physTemperatur, 
			                                   double windgeschwindigkeit ) 
					throws WindChillException {
		
		if ( physTemperatur < -50.0 || physTemperatur > 10.0 ) {
			
			final String fehlerText = 
					"Physische Temperatur " + physTemperatur + 
					" Grad Celsius ausserhalb Bereich -50.0 und 10.0 Grad Celsius.";
			
			LOG.error( fehlerText );
			
			throw new WindChillException( fehlerText ); 
		}
		if ( windgeschwindigkeit < 5.0 || windgeschwindigkeit > 60.0 ) {

			final String fehlerText = 
					"Windgeschwindigkeit " + windgeschwindigkeit + 
					" km/h ausserhalb Bereich 5.0 km/h und 60 km/h.";
			
			LOG.error( fehlerText );
			
			throw new WindChillException( fehlerText );			
		}
		
		
	    final double v  = Math.pow( windgeschwindigkeit, 0.16 );
	    final double va = physTemperatur;

	    final double gefuehlteTemperatur        = 13.12 + 0.6215 * va + (0.3965 * va - 11.37) * v;
	    final double gefuehlteTemperaturGerundet = Math.round( gefuehlteTemperatur * 10 ) / 10;

	    return gefuehlteTemperaturGerundet;
	}
	
}
