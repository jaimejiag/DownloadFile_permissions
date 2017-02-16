package com.usuario.downloaderfile;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DownloaderFragment extends Fragment implements View.OnClickListener{
    private static final int REQUEST_WRITE_EXTERNAL = 1;
    private Button btnDownload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloader, container, false);
        btnDownload = (Button) view.findViewById(R.id.btn_download);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDownload.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (checkWritePermission())
            onDownload();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                onDownload();
        }
    }


    /**
     * Método que comprueba si la app tiene permisos
     * necesarios para ejecutarse.
     * @return
     */
    private boolean checkWritePermission() {
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int permissioncheck = ContextCompat.checkSelfPermission(getActivity(), permission);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED)
            return  true;

        //Si ha sido denegado los permisos por segunda vez.
        else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            Snackbar.make(
                    getActivity().findViewById(R.id.layout_fragment),
                    "Dame los permisos, me cago en la leche Merche",
                    Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{permission}, REQUEST_WRITE_EXTERNAL);
                        }
                    }).show();

            return  false;

        //Si ha sido denegado los permisos por primera vez
        } else {
            requestPermissions(new String[]{permission},
                    REQUEST_WRITE_EXTERNAL);
            return false;
        }
    }


    /**
     * Método que inicia la descarga de un fichero.
     */
    private void onDownload() {
        btnDownload.setEnabled(false);
        Intent intent = new Intent(getActivity(), Downloader.class);
        intent.setData(Uri.parse("https://commonsware.com/Android/Android-1_0-CC.pdf"));
        getActivity().startService(intent);
    }
}
