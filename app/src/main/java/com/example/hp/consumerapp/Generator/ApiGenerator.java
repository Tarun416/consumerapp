package com.example.hp.consumerapp.Generator;

import android.content.Context;
import android.util.Log;

import com.example.hp.consumerapp.Logger;
import com.squareup.okhttp.CipherSuite;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.TlsVersion;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

//import android.util.Base64;

/**
 * Created by rahul on 09/03/16.
 */
public class ApiGenerator {

    private static String KEY_STORE_PATH = "/Users/rahul/Downloads/myapp_keyandcertbundle.p12";
    private static String KEY_STORE_PASSWORD = "abcd";
   static SSLContext sslContext;
   static TrustManagerFactory trustManagerFactory;
    static KeyManagerFactory keyManagerFactory;
    static KeyStore ks;
    static CertificateFactory cf;
    static Certificate ca;



    static KeyStore keyStore;
    Context context;
   static SSLSocketFactory NoSSLv3Factory;
    static TrustManager[] trustAllCerts;


    public static String BASE_URL = "https://sandbox.api.visa.com/visadirect/";

    // No need to instantiate this class.
    private ApiGenerator() {

    }


    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {

        // Create a trust manager that does not validate certificate chains
         trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
    }


    public static <S> S createService(Class<S> serviceClass, InputStream cert, InputStream bundle) {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(


                       CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA,
                       // CipherSuite.
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();


        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
                request.addHeader("Authorization", getBasicAuthHeader("44OXX2XY9UPKWU35RI6V21m4iFR9JznUk4GACR91spb4iiclE", "8N991FqhI8UiY24v11BZ1w2MffDC3NV8bY0Q2bw"));

            }
        };
        OkHttpClient okHttpClient = new OkHttpClient();

      //  OkHttpClient client = new OkHttpClient();
        KeyStore keyStore = readKeyStore(cert , bundle); //your method to obtain KeyStore

        try {
             sslContext = SSLContext.getInstance("TLSv1");


             trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        }

        catch(NoSuchAlgorithmException e)
        {
          Log.d("dddd",e.getMessage());
        }

        try {
            trustManagerFactory.init(keyStore);
        }
        catch(KeyStoreException e)
        {

        }

        try {
           keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        }
        catch(NoSuchAlgorithmException e)
        {

        }
        try {
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e)
        {

        }











        try {
            //sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new SecureRandom());

          //   NoSSLv3Factory = new NoSSLv3SocketFactory(sslContext.getSocketFactory());

        }
        catch(KeyManagementException e)
        {

        }



      // okHttpClient.setSslSocketFactory(NoSSLv3Factory);

        // NoSSLv3Factory = new NoSSLv3SocketFactory(sslContext.getSocketFactory());
       okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
       // okHttpClient.setSocketFactory(NoSSLv3Factory);

                okHttpClient.setConnectionSpecs(Collections.singletonList(spec));
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.d("Retro", msg);
                    }
                })
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(okHttpClient));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }

    private static KeyStore readKeyStore(InputStream cert, InputStream bundle) {

      /*  try {

           //  ks = KeyStore.getInstance("PKCS12");

       //   String  ks = KeyStore.getDefaultType();




        }
        catch (KeyStoreException e)
        {

        }

        try {*/


        try {
             cf = CertificateFactory.getInstance("X.509");



        } catch (CertificateException e)

        {
            Log.d("errorcf",e.getMessage());
        }
           // InputStream cert = DemoApplication.geContext().getResources().openRawResource(R.raw.myapp_keyandcertbundle);

            try {
                Log.d("gg",cert.toString());
                ca = cf.generateCertificate(cert);

                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                try {
                    cert.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* URL url = null;
                try {
                    url = new URL("https://example.com");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection urlConnection =
                        null;
                try {
                    urlConnection = (HttpsURLConnection)url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    cert = urlConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] responsedata = toByteArray(cert);
                Log.w("TAG", "response is "+convertBytesToHexString(responsedata));
                try {
                    cert.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            } catch (CertificateException e) {
                e.printStackTrace();
            } finally{
                try {
                    cert.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


           // String keyStoreType = ks.getDefaultType();
            try {
                 keyStore = KeyStore.getInstance("PKCS12");
            }
            catch(KeyStoreException e)
            {

            }
        try {
            keyStore.load(bundle, "abcd".toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        //  InputStream i=new FileInputStream(String.valueOf(DemoApplication.geContext().getResources().openRawResource(R.raw.myapp_keyandcertbundle)));

           // ks.load(i, KEY_STORE_PASSWORD.toCharArray());
       /* }
        catch (  NoSuchAlgorithmException | IOException | java.security.cert.CertificateException e)
        {
            e.printStackTrace();
            Log.d("error",e.getMessage());
        }*/

        return keyStore;




    }

    private static String convertBytesToHexString(byte[] responsedata) {
        char[] c = new char[responsedata.length * 2];
        int b;
        for (int i = 0; i < responsedata.length; i++) {
            b = responsedata[i] >> 4;
            c[i * 2] = (char)(55 + b + (((b-10)>>31)&-7));
            b = responsedata[i] & 0xF;
            c[i * 2 + 1] = (char)(55 + b + (((b-10)>>31)&-7));
        }
        return new String(c);
    }

    private static byte[] toByteArray(InputStream cert) {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        try {
            while ((nRead = cert.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toByteArray();

    }

    private static String getBasicAuthHeader(String s, String s1) {




        return "Basic " + base64Encode(s + ":" + s1);
    }

    private static String base64Encode(String s) {
        byte[] encodedBytes = Base64.encodeBase64(s.getBytes());
        return new String(encodedBytes, Charset.forName("UTF-8"));
    }

    public static <S> S createRevocableService(Class<S> serviceClass) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
                request.addHeader("Authorization", getBasicAuthHeader("44OXX2XY9UPKWU35RI6V21m4iFR9JznUk4GACR91spb4iiclE", "8N991FqhI8UiY24v11BZ1w2MffDC3NV8bY0Q2bw"));
            }
        };


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Logger.d("Retro", msg);
                    }
                })
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }

}

