package de.lars_kadel.thc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class SettingsDialog extends AppCompatDialogFragment {
    public interface  SettingsInterface{
        public void sendSettings(int preptime, int gametime, int tratio, int dratio);
    }

    private SettingsInterface parent;
    private SeekBar preptimeSlider;
    private SeekBar gametimeSlider;
    private SeekBar tratioSlider;
    private SeekBar dratioSlider;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        parent = (Lobby) getActivity();
        final View v = inflater.inflate(R.layout.dialog_settings,null);
        final TextView preptimeText = v.findViewById(R.id.preptimeText);
        final TextView gametimeText = v.findViewById(R.id.gametimeText);
        final TextView tratioText = v.findViewById(R.id.tratioText);
        final TextView dratioText = v.findViewById(R.id.dratioText);
        preptimeSlider = v.findViewById(R.id.preptimeSlider);
        gametimeSlider = v.findViewById(R.id.gametimeSlider);
        tratioSlider = v.findViewById(R.id.tratioSlider);
        dratioSlider = v.findViewById(R.id.dratioSlider);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean byUser) {
                if(seekBar.equals(preptimeSlider)){
                    preptimeText.setText(getText(R.string.preptime)+" - "+(value/60)+":"+value%60);
                }else if(seekBar.equals(gametimeSlider)){
                    gametimeText.setText(getText(R.string.gametime)+" - "+(value*10/60)+":"+value*10%60);
                }else if(seekBar.equals(tratioSlider)){
                    tratioText.setText(getText(R.string.tratio)+" - "+value+"%");
                }else if(seekBar.equals(dratioSlider)){
                    dratioText.setText(getText(R.string.dratio)+" - "+value+"%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        preptimeSlider.setOnSeekBarChangeListener(listener);
        gametimeSlider.setOnSeekBarChangeListener(listener);
        tratioSlider.setOnSeekBarChangeListener(listener);
        dratioSlider.setOnSeekBarChangeListener(listener);

        builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        parent.sendSettings(preptimeSlider.getProgress(),
                                            gametimeSlider.getProgress(),
                                            tratioSlider.getProgress(),
                                            dratioSlider.getProgress());
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                    }});


        return builder.create();
    }
}
