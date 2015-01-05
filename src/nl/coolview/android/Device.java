package nl.coolview.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * Device Class
 * 
 * <p>Copyright (c) Rory Slegtenhorst
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Rory Slegtenhorst <rory.slegtenhorst@gmail.com>
 */
public final class Device {

	public static Boolean isEmulator() {
		return Build.BRAND.equals("generic") && Build.DEVICE.equals("generic");
	}

	public static String getDeviceID(Context context) {
        //1 compute IMEI
		String mImei = null;
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyMgr != null) {
        	mImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
        }
    	
        //2 compute DEVICE ID
        String mDevice = "35" + //we make this look like a valid IMEI
        	Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + 
        	Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + 
        	Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + 
        	Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + 
        	Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + 
        	Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + 
        	Build.USER.length() % 10; //13 digits

        //3 android ID - unreliable
        String mAndroid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
        
        //4 wifi manager, read MAC address - requires  android.permission.ACCESS_WIFI_STATE or comes as null
        String mWifi = null;
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
        	mWifi = wm.getConnectionInfo().getMacAddress();
        }

        //5 Bluetooth MAC address  android.permission.BLUETOOTH required and SDK >= 8 required (android 2.1 update 1)
        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
    	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	String mBlueTooth = null;
    	if (m_BluetoothAdapter != null) {
    		mBlueTooth = m_BluetoothAdapter.getAddress();
    	}
    	
    	//6 SUM THE IDs
    	String mLong = mImei + mDevice + mAndroid + mWifi + mBlueTooth;
    	MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		m.update(mLong.getBytes(), 0, mLong.length());
		byte p_md5Data[] = m.digest();
		
		String mUnique = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper padding)
			if (b <= 0xF) mUnique += "0";
			// add number to string
			mUnique += Integer.toHexString(b); 
		}
		mUnique = mUnique.toUpperCase();

		return mUnique;
	}
}
