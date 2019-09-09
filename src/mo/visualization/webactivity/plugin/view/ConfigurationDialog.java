package mo.visualization.webactivity.plugin.view;

import mo.core.I18n;
import mo.core.ui.Utils;
import mo.visualization.webactivity.plugin.model.VisualizationConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationDialog extends JDialog {
    private VisualizationConfiguration temporalConfig;
    private boolean accepted;
    private JLabel configurationNameLabel, configurationNameErrorLabel;
    private JTextField configurationNameTextField;
    private JButton saveConfigButton;
    private I18n i18n;

    public ConfigurationDialog(){
        super(null,"", Dialog.ModalityType.APPLICATION_MODAL);
        this.temporalConfig = null;
        this.accepted = false;
        this.i18n = new I18n(ConfigurationDialog.class);
        this.setTitle(this.i18n.s("configurationFrameTitleText"));
        this.setLayout(new GridBagLayout());
        this.initComponents();
        this.addComponents();
        this.addActionListeners();
    }

    private void initComponents() {
        this.configurationNameLabel = new JLabel(this.i18n.s("configurationNameLabelText"));
        this.configurationNameTextField = new JTextField();
        this.configurationNameErrorLabel = new JLabel();
        this.configurationNameErrorLabel.setVisible(false);
        this.saveConfigButton = new JButton(this.i18n.s("saveConfigButtonText"));
    }

    private void addComponents() {
        Container contentPane = this.getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();

        /* Configuration name Label*/
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.setConstraintsForLeftSide(constraints, true);
        constraints.insets = new Insets(10,10,5,5);
        contentPane.add(this.configurationNameLabel, constraints);

        /* Configuration Name Edit Text*/
        constraints = new GridBagConstraints();
        constraints.gridx=1;
        constraints.gridy=0;
        this.setConstraintsForRightSide(constraints, false);
        constraints.insets= new Insets(10,5,5,10);
        contentPane.add(this.configurationNameTextField);

        /* Coonfiguration Name error Label*/
        constraints = new GridBagConstraints();
        constraints.gridx=1;
        constraints.gridy=1;
        this.setConstraintsForRightSide(constraints, true);
        contentPane.add(this.configurationNameErrorLabel, constraints);

        /* Save Configuration button*/
        constraints = new GridBagConstraints();
        this.setConstraintsForSaveButton(constraints);
        contentPane.add(this.saveConfigButton, constraints);
    }

    public void showDialog(){
        setMinimumSize(new Dimension(400, 150));
        setPreferredSize(new Dimension(400, 300));
        pack();
        Utils.centerOnScreen(this);
        this.setVisible(true);
    }

    private void addActionListeners(){
        this.saveConfigButton.addActionListener(e -> {
            this.configurationNameErrorLabel.setText("");
            this.configurationNameErrorLabel.setVisible(false);
            String configurationName = this.configurationNameTextField.getText();
            if(configurationName.isEmpty()){
                this.configurationNameErrorLabel.setText("configurationNameErrorLabelText");
                this.configurationNameErrorLabel.setVisible(true);
                return;
            }
            this.setVisible(false);
            this.dispose();
            this.temporalConfig = new VisualizationConfiguration(configurationName);
            this.accepted = true;
            this.setVisible(false);
            this.dispose();
        });
    }

    public VisualizationConfiguration getTemporalConfig() {
        return this.temporalConfig;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    private void setConstraintsForLeftSide(GridBagConstraints constraints, boolean hasErrorLabel){
        constraints.gridwidth=1;
        constraints.gridheight= hasErrorLabel ? 2 : 1;
        constraints.weighty=1.0;
        constraints.insets= new Insets(5,10,5,5);
        constraints.anchor=GridBagConstraints.FIRST_LINE_START;
    }

    private void setConstraintsForRightSide(GridBagConstraints constraints, boolean errorLabel){
        constraints.gridheight=1;
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        constraints.weightx=1.0;
        constraints.fill= GridBagConstraints.HORIZONTAL;
        int topInset = errorLabel ? 0 : 5;
        int bottomInset = errorLabel ? 5 : 0;
        constraints.insets= new Insets(topInset,5,bottomInset,10);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
    }

    private void setConstraintsForSaveButton(GridBagConstraints constraints){
        constraints.gridx= 0;
        constraints.gridy=2;
        constraints.gridheight=1;
        constraints.gridwidth=2;
        constraints.weightx=0.0;
        constraints.weighty=0.0;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.insets= new Insets(-10,10,10,10);
    }

}
