package br.com.editorimagens.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Slider;

public class ImageController implements Initializable {

	@FXML ImageView imgFoto;
	@FXML Button btnCarregarImagem;

	List<Image> lstImagens = new ArrayList<>();

	int ponteiro = -1;
	@FXML Button btnProximo;
	@FXML Button btnAnterior;

	ColorAdjust efeitoCor = new ColorAdjust();
	@FXML Slider sldContraste;
	@FXML Slider sldBrilho;
	@FXML Slider sldSaturacao;
	@FXML Slider sldTonalidade;
	@FXML Button btnSalvar;


	@FXML public void carregarImagem() {

		//objeto que abre o windows explorer
		FileChooser explorer = new FileChooser();

		//Filtro de extensões de arquivo
		explorer.getExtensionFilters().add(
				new ExtensionFilter("Imagens", "*jpg","*png", "*gif")
		);

		//obter a janela atual
		Window janela = btnCarregarImagem.getScene().getWindow();

		//abrir o explorer para escolher arquivo
		File arquivo = explorer.showOpenDialog(janela);

		if(arquivo != null){
			System.out.println(arquivo);

			//criar o objeto Image
			Image foto = new Image("file:"+arquivo.getAbsolutePath());

			imgFoto.setImage(foto);

			//guardando imagem na lista
			lstImagens.add(foto);

			ponteiro++;

			btnAnterior.setDisable(false);
			btnProximo.setDisable(false);

			ajustarValoresPadraoSlider();
			boolean nao = false;
			desabilitarComponentes(nao);

		}
	}

	@FXML public void carregarAnterior() {

		if(ponteiro==0){
			ponteiro = lstImagens.size() -1;
		}else{
			ponteiro--;
		}

		Image foto= lstImagens.get(ponteiro);
		imgFoto.setImage(foto);
		ajustarValoresPadraoSlider();
	}

	@FXML public void carregarProximo() {
		//verificando se o pointeiro está
		//na ultima posição
		if(ponteiro == lstImagens.size()-1){
			ponteiro =0;
		}else{
			ponteiro++;
		}
		Image foto= lstImagens.get(ponteiro);
		imgFoto.setImage(foto);
		ajustarValoresPadraoSlider();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		imgFoto.setEffect(efeitoCor);


		ajustarValoresPadraoSlider();

		adicionarListenersSlider();

		boolean sim = true;
		desabilitarComponentes(sim);

	}

	private void desabilitarComponentes(boolean desabilita){

		btnAnterior.setDisable(desabilita);
		btnProximo.setDisable(desabilita);

		sldBrilho.setDisable(desabilita);
		sldContraste.setDisable(desabilita);
		sldSaturacao.setDisable(desabilita);
		sldTonalidade.setDisable(desabilita);
		btnSalvar.setDisable(desabilita);
	}

	private void adicionarListenersSlider(){
		sldContraste.valueProperty().addListener( x->{
			//System.out.println(sldContraste.getValue());
			efeitoCor.setContrast(sldContraste.getValue());
		});

		sldBrilho.valueProperty().addListener( x->{
			//System.out.println(sldContraste.getValue());
			efeitoCor.setBrightness(sldBrilho.getValue());
		});

		sldSaturacao.valueProperty().addListener( x->{
			//System.out.println(sldContraste.getValue());
			efeitoCor.setSaturation(sldSaturacao.getValue());
		});

		sldTonalidade.valueProperty().addListener( x->{
			//System.out.println(sldContraste.getValue());
			efeitoCor.setHue(sldTonalidade.getValue());
		});
	}

	private void ajustarValoresPadraoSlider(){
		//ajustar valores padrão p slider
		sldContraste.setMin(-1);
		sldContraste.setMax(1);
		sldContraste.setValue(0);

		sldBrilho.setMin(-1);
		sldBrilho.setMax(1);
		sldBrilho.setValue(0);

		sldSaturacao.setMin(-1);
		sldSaturacao.setMax(1);
		sldSaturacao.setValue(0);

		sldTonalidade.setMin(-1);
		sldTonalidade.setMax(1);
		sldTonalidade.setValue(0);

	}

	@FXML public void restaurarPadrao() {
		ajustarValoresPadraoSlider();
	}

	@FXML public void salvarImagem() {


		FileChooser saveFile = new FileChooser();
		//Filtro de extensões de arquivo
		saveFile.getExtensionFilters().add(
			new ExtensionFilter("Imagens", "*jpg","*png", "*gif")
		);

		//obter a janela
		Window w = imgFoto.getScene().getWindow();
		File output = saveFile.showSaveDialog(w);

		//tira um 'print' do imageview e salva
		//em uma imagebuffer(imagem na memoria)
		BufferedImage bImage = SwingFXUtils.
				fromFXImage( imgFoto.snapshot(null, null)  , null);


		try {
			//Grava realmente o arquivo na pasta
			ImageIO.write(bImage, "png",
					new File(output.toString()+".png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(null, "Salvo com sucesso!");

	}
}
