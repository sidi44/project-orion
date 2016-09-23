package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

class SliderPanel extends Table {

    private final Label nameLabel;
    private final Label valueLabel;
    private final Slider slider;
    private int value;

    SliderPanel(Label nameLabel,
                Label valueLabel,
                Button minusButton,
                Button plusButton,
                Slider slider,
                float padding) {
        super();

        this.nameLabel = nameLabel;
        this.valueLabel = valueLabel;
        this.slider = slider;

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
