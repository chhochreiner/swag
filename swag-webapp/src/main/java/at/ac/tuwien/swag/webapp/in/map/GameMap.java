package at.ac.tuwien.swag.webapp.in.map;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import at.ac.tuwien.swag.model.domain.MapUser;
import at.ac.tuwien.swag.model.domain.Square;

public class GameMap extends Panel {
    private static final long serialVersionUID = -2473064801663338918L;
    
    private ListView<List<Square>> gameMaplistView;
    private IModel<List<List<Square>>> gameMapList;
    private MapUser mapUser;
	private MapModalWindow mapModalWindow;

    public GameMap(String id, MapUser mapUser, IModel<List<List<Square>>> gameMapList) {
        super(id);

        this.mapUser = mapUser;
        this.gameMapList = gameMapList;

        this.setOutputMarkupId(true);

        this.setupGameMapView();
        this.add(gameMaplistView);
        
        this.setupMapModalWindow();
        this.add(mapModalWindow);
    }

    private void setupMapModalWindow() {
    	 // The ModalWindow, showing some choices for the user to select.
        mapModalWindow = new MapModalWindow("modalwindow"){
        	private static final long serialVersionUID = 6244873170722607468L;

			void onSelect(AjaxRequestTarget target, String selection) {
            // Handle Select action
           //     close(target);
			}

			void onCancel(AjaxRequestTarget target) {
                // Handle Cancel action
              //  close(target);
            }
        };
    }
    
    private void setupGameMapView() {
        gameMaplistView = new ListView<List<Square>>("gameMap", gameMapList) {
            private static final long serialVersionUID = 7083713778515545799L;

            @Override
            protected void populateItem(ListItem<List<Square>> row) {

                List<Square> rowList = row.getModelObject();

                ListView<Square> rowListView = new ListView<Square>("row", rowList) {
                    private static final long serialVersionUID = 3054181382288233598L;

                    @Override
                    protected void populateItem(ListItem<Square> squareList) {

                        final Square square = squareList.getModelObject();

                        Label label = null;
                        if (mapUser != null && mapUser.getSquares().contains(square)) {

                            label = new Label("square", "X: " + square.getCoordX() + " AAAAA Y: " + square.getCoordY());
                            if (checkIfBaseBuildings(square)) {
                                label = new Label("square", "BASEOWNEDBYME");
                                label.add(new SimpleAttributeModifier("class", "baseSquare"));
                            }
                            if (square.getIsHomeBase()) {
                                label = new Label("square", "HOMEBASE");
                                label.add(new SimpleAttributeModifier("class", "homeBaseSquare"));
                            }
                        } else {
                            if (checkIfBaseBuildings(square)) {
                                label = new Label("square", "X: " + square.getCoordX() +" Y: " + square.getCoordY());
                                label.add(new SimpleAttributeModifier("class", "baseSquare"));
                            }

                            label =new Label("square", "X: " + square.getCoordX() + " EMPTY  Y: " + square.getCoordY());
                        }

                        squareList.add(label);
                        
/////////////////////////////////////////// TEST MODAL WINDOW ////////////////////////////////////////////////
                        AjaxFallbackLink<String> squareLink = new AjaxFallbackLink<String>("squareLink") {
                			private static final long serialVersionUID = -2641432580203719830L;
                			@Override
                			public void onClick(AjaxRequestTarget target) {
                				//selectModalWindow.setSquareId(square.getId());
                				mapModalWindow.loadBasePanel(square.getId());
                				mapModalWindow.show(target);
                				 
                			}
                		};			       
/////////////////////////////////////////// TEST MODAL WINDOW ////////////////////////////////////////////////

							squareLink.setOutputMarkupId(true);
							squareList.add(squareLink);
							 									
							label.setOutputMarkupId(true);
							squareLink.add(label);
                    }
                };
                row.add(rowListView);
            }
        };
    }

    /**
     * 
     * @param sq
     * @return
     */
    private boolean checkIfBaseBuildings(Square sq) {
        if (sq.getBuildings() == null || sq.getBuildings().isEmpty()) {
            return false;
        }
        return true;
    }
}
