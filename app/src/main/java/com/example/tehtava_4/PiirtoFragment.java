package com.example.tehtava_4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import com.google.android.material.slider.Slider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PiirtoFragment extends Fragment {
    private PiirtoNakyma piirtoNakyma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_piirto, container, false);

        piirtoNakyma = view.findViewById(R.id.piirtoNakyma);
        Slider pensselinKokoSlider = view.findViewById(R.id.pensselinKokoSlider);
        RadioGroup toolGroup = view.findViewById(R.id.toolRadioGroup);

        pensselinKokoSlider.addOnChangeListener((slider1, value, fromUser) ->
                piirtoNakyma.updateBrush(value, Color.BLACK)
        );

        toolGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioKyna) {
                piirtoNakyma.currentTool = PiirtoNakyma.ToolType.PEN;
            } else if (checkedId == R.id.radioYmpyra) {
                piirtoNakyma.currentTool = PiirtoNakyma.ToolType.CIRCLE;
            } else if (checkedId == R.id.radioNelio) {
                piirtoNakyma.currentTool = PiirtoNakyma.ToolType.SQUARE;
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_piirros, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            tallennaTiedostoon();
            return true;
        } else if (item.getItemId() == R.id.action_clear) {
            piirtoNakyma.reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void tallennaTiedostoon() {
        Bitmap bitmap = Bitmap.createBitmap(piirtoNakyma.getWidth(),
                piirtoNakyma.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        piirtoNakyma.draw(canvas);

        File file = new File(requireContext().getFilesDir(), "oma_piirros_" + System.currentTimeMillis() + ".png");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            android.widget.Toast.makeText(getContext(), "Tallennettu: " + file.getName(), android.widget.Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
