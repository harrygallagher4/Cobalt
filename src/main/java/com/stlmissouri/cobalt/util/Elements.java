package com.stlmissouri.cobalt.util;

import java.util.Random;

/**
 * User: Stl
 * Date: 7/15/2014
 * Time: 12:18 AM
 * Use:  Generates a random element name
 */
public class Elements {

    private static final String[] ELEMENTS = "Calcium Californium Carbon Cerium Cesium Chlorine Chromium Cobalt Copernicium Copper Curium Darmstadtium Dubnium Dysprosium Einsteinium Erbium Europium Fermium Flerovium Fluorine Francium Gadolinium Gallium Germanium Gold Hafnium Hassium Helium Holmium Hydrogen Indium Iodine Iridium Iron Krypton Lanthanum Lawrencium Lead Lithium Livermorium Lutetium Magnesium Manganese Meitnerium Mendelevium Mercury Molybdenum Neodymium Neon Neptunium Nickel Niobium Nitrogen Nobelium Osmium Oxygen Palladium Phosphorus Platinum Plutonium Polonium Potassium Praseodymium Promethium Protactinium Radium Radon Rhenium Rhodium Roentgenium Rubidium Ruthenium Rutherfordium Samarium Scandium Seaborgium Selenium Silicon Silver Sodium Strontium Sulfur Tantalum Technetium Tellurium Terbium Thallium Thorium Thulium Tin Titanium Tungsten Uranium Vanadium Xenon Ytterbium Yttrium Zinc Zirconium".split(" ");
    private static final Random RANDOM = new Random();

    public static String randomElement() {
        return ELEMENTS[RANDOM.nextInt(ELEMENTS.length - 1)];
    }

}
