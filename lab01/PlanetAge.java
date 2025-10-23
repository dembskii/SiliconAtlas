public class PlanetAge {
    public enum Planet {
        ZIEMIA(1.0),
        MERKURY(0.2408467),
        WENUS(0.61519726),
        MARS(1.8808158),
        JOWISZ(11.862615),
        SATURN(29.447498),
        URAN(84.016846),
        NEPTUN(164.79132);

        private final double orbitalPeriodRelativeToEarth;

        Planet(double orbitalPeriodRelativeToEarth) {
            this.orbitalPeriodRelativeToEarth = orbitalPeriodRelativeToEarth;
        }

        public double getOrbitalPeriodRelativeToEarth() {
            return orbitalPeriodRelativeToEarth;
        }

        public static Planet fromString(String name) {
            if (name == null) throw new IllegalArgumentException("Nazwa planety nie może być null");
            switch (name.toLowerCase()) {
                case "ziemia":
                    return ZIEMIA;
                case "merkury":
                    return MERKURY;
                case "wenus":
                    return WENUS;
                case "mars":
                    return MARS;
                case "jowisz":
                    return JOWISZ;
                case "saturn":
                    return SATURN;
                case "uran":
                    return URAN;
                case "neptun":
                    return NEPTUN;
                default:
                    throw new IllegalArgumentException("Nieznana planeta: " + name);
            }
        }
    }

    public static double obliczWiekNaPlanecie(double sekundy, Planet planeta) {
        final double SECONDS_IN_EARTH_YEAR = 31557600.0;
        double lataZiemskie = sekundy / SECONDS_IN_EARTH_YEAR;
        return lataZiemskie / planeta.getOrbitalPeriodRelativeToEarth();
    }
}
