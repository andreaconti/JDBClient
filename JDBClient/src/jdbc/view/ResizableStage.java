package jdbc.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdbc.view.css.CSSStyleable;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

class ResizableStage extends Stage implements CSSStyleable {
	
	private BorderPane root = new BorderPane();
	private ObservableList<String> cssPaths;
	
	Button close;
	Button minimize;
	Button allScreen;
	
	public ResizableStage() {
		
		super();
		this.initStyle(StageStyle.UNDECORATED);
		
		root = new BorderPane();
		cssPaths = FXCollections.observableArrayList();
		
		this.sceneProperty().addListener( (obs, oldV, newV) -> {
			root.setTop(createBar());
			root.setCenter(newV.getRoot());
			newV.setRoot(root);
			ResizeHelper.addResizeListener(this);
			
			for ( String path : cssPaths ) {
				if (!newV.getStylesheets().contains(path))
					newV.getStylesheets().add(path);
			}
			
			cssPaths = newV.getStylesheets();
			
		});
		
	}
	
	private HBox createBar() {
		
		close = new Button();
		minimize = new Button();
		allScreen = new Button();
		
		HBox bar = new HBox(3);
		bar.getStyleClass().add(".bar");
		bar.getChildren().addAll(close, minimize, allScreen);
		
		close.setPrefWidth(30);
		close.setOnMouseEntered( ev -> close.setText("x") );
		close.setOnMouseExited( ev -> close.setText("") );
		close.setStyle(
				"-fx-background-color: rgb(255,0,0);"+
				"-fx-background-insets: 5,0,5,0;"+
				"-fx-text-fill: rgb(255,255,255);" + 
				"-fx-font-size: 1em" );
		
		minimize.setPrefWidth(30);
		minimize.setOnMouseEntered( ev -> minimize.setText("-") );
		minimize.setOnMouseExited( ev -> minimize.setText("") );
		minimize.setStyle(
				"-fx-background-color: rgb(42,203,52);"+
				"-fx-background-insets: 5,0,5,0;"+
				"-fx-text-fill: rgb(255,255,255);" + 
				"-fx-font-size: 1em");
		
		allScreen.setPrefWidth(30);
		allScreen.setOnMouseEntered( ev -> allScreen.setText("•") );
		allScreen.setOnMouseExited( ev -> allScreen.setText("") );
		allScreen.setStyle(
				"-fx-background-color: rgb(254,181,37);"+
				"-fx-background-insets: 5,0,5,0;"+
				"-fx-text-fill: rgb(255,255,255);" +
				"-fx-font-size: 1em");
		
		allScreen.setOnAction( ev -> this.setFullScreen(true) );
		
		EffectUtilities.makeDraggable(this, bar);
		
		return bar;
	}
	
	public void setMinimizeButtonVisible(boolean hide) {
		this.minimize.setVisible(hide);
	}

	public void setAllScreenButtonVisible(boolean hide) {
		this.allScreen.setVisible(hide);
	}
	
	@Override
	public void setCSSStyle(List<String> cssPaths) {
		for (String path : cssPaths)
			addCSSStyle(path);
	}

	@Override
	public void addCSSStyle(String cssPath) {
		cssPaths.add(cssPath);
	}

	@Override
	public void resetCSSStyle() {
		cssPaths.removeAll(cssPaths);
	}

	@Override
	public void removeCSSStyle(String cssPath) {
		cssPaths.remove(cssPath);
	}

}

class ResizeHelper {

    public static void addResizeListener(Stage stage) {
        ResizeListener resizeListener = new ResizeListener(stage);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
        ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (Node child : children) {
            addListenerDeeply(child, resizeListener);
        }
    }

    public static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    static class ResizeListener implements EventHandler<MouseEvent> {
        private Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private int border = 4;
        private double startX = 0;
        private double startY = 0;

        public ResizeListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            Scene scene = stage.getScene();

            double mouseEventX = mouseEvent.getSceneX(), 
                   mouseEventY = mouseEvent.getSceneY(),
                   sceneWidth = scene.getWidth(),
                   sceneHeight = scene.getHeight();

            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType) == true) {
                if (mouseEventX < border && mouseEventY < border) {
                    cursorEvent = Cursor.NW_RESIZE;
                } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SW_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                    cursorEvent = Cursor.NE_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SE_RESIZE;
                } else if (mouseEventX < border) {
                    cursorEvent = Cursor.W_RESIZE;
                } else if (mouseEventX > sceneWidth - border) {
                    cursorEvent = Cursor.E_RESIZE;
                } else if (mouseEventY < border) {
                    cursorEvent = Cursor.N_RESIZE;
                } else if (mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.S_RESIZE;
                } else {
                    cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(cursorEvent);
            } else if(MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)){
                scene.setCursor(Cursor.DEFAULT);
            } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {
                startX = stage.getWidth() - mouseEventX;
                startY = stage.getHeight() - mouseEventY;
            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {
                if (Cursor.DEFAULT.equals(cursorEvent) == false) {
                    if (Cursor.W_RESIZE.equals(cursorEvent) == false && Cursor.E_RESIZE.equals(cursorEvent) == false) {
                        double minHeight = stage.getMinHeight() > (border*2) ? stage.getMinHeight() : (border*2);
                        if (Cursor.NW_RESIZE.equals(cursorEvent) == true || Cursor.N_RESIZE.equals(cursorEvent) == true || Cursor.NE_RESIZE.equals(cursorEvent) == true) {
                            if (stage.getHeight() > minHeight || mouseEventY < 0) {
                                stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                                stage.setY(mouseEvent.getScreenY());
                            }
                        } else {
                            if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {
                                stage.setHeight(mouseEventY + startY);
                            }
                        }
                    }

                    if (Cursor.N_RESIZE.equals(cursorEvent) == false && Cursor.S_RESIZE.equals(cursorEvent) == false) {
                        double minWidth = stage.getMinWidth() > (border*2) ? stage.getMinWidth() : (border*2);
                        if (Cursor.NW_RESIZE.equals(cursorEvent) == true || Cursor.W_RESIZE.equals(cursorEvent) == true || Cursor.SW_RESIZE.equals(cursorEvent) == true) {
                            if (stage.getWidth() > minWidth || mouseEventX < 0) {
                                stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                                stage.setX(mouseEvent.getScreenX());
                            }
                        } else {
                            if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {
                                stage.setWidth(mouseEventX + startX);
                            }
                        }
                    }
                }

            }
        }
    }
}

/** Various utilities for applying different effects to nodes. */
class EffectUtilities {
  /** makes a stage draggable using a given node */
  public static void makeDraggable(final Stage stage, final Node byNode) {
    final Delta dragDelta = new Delta();
    byNode.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        // record a delta distance for the drag and drop operation.
        dragDelta.x = stage.getX() - mouseEvent.getScreenX();
        dragDelta.y = stage.getY() - mouseEvent.getScreenY();
        byNode.setCursor(Cursor.MOVE);
      }
    });
    byNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        byNode.setCursor(Cursor.HAND);
      }
    });
    byNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + dragDelta.x);
        stage.setY(mouseEvent.getScreenY() + dragDelta.y);
      }
    });
    byNode.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        if (!mouseEvent.isPrimaryButtonDown()) {
          byNode.setCursor(Cursor.DEFAULT);
        }
      }
    });
    byNode.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent mouseEvent) {
        if (!mouseEvent.isPrimaryButtonDown()) {
          byNode.setCursor(Cursor.DEFAULT);
        }
      }
    });
  }

  /** records relative x and y co-ordinates. */
  private static class Delta {
    double x, y;
  }
}



