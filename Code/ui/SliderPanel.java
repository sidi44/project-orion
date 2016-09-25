package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

class SliderPanel extends Table {

    SliderPanel(Label nameLabel,
                Label valueLabel,
                Button minusButton,
                Button plusButton,
                Slider slider,
                float padding) {
        super();

        this.add(nameLabel).pad(padding);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(valueLabel);
        verticalGroup.addActor(slider);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(minusButton);
        horizontalGroup.addActor(verticalGroup);
        horizontalGroup.addActor(plusButton);

        this.add(horizontalGroup).pad(padding);
    }

}
