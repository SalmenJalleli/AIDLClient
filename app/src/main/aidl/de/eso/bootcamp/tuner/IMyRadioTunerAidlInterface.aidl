// IMyRadioTunerAidlInterface.aidl
package de.eso.bootcamp.tuner;

// Declare any non-default types here with import statements

interface IMyRadioTunerAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /*void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    int getPid();*/
    String startPlayback();
    String stopPlayback();
    boolean playbackIsRunning();
}
