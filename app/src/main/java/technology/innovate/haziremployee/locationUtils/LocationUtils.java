package technology.innovate.haziremployee.locationUtils;

import android.location.Location;

public class LocationUtils {

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta < TWO_MINUTES;
        float accuracyDelta = (float) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isMoreAccurate = accuracyDelta < 0.0f;

        if (isSignificantlyNewer && isMoreAccurate) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
