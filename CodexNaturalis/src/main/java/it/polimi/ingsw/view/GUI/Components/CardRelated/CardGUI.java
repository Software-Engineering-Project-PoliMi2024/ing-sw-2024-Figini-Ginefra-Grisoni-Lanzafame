package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

public class CardGUI {
    private LightCard target;
    protected CardFace face;
    private final ImageView imageView = new ImageView();
    private Image image;

    private double tx, ty;

    private final Object lock = new Object();

    private final AtomicBoolean farFromTarget = new AtomicBoolean(true);

    private Thread translationThread;


    public CardGUI(LightCard target, CardFace face) {
        this.target = target;
        this.face = face;
        this.update();

        imageView.setFitWidth(image.getWidth() * 0.3);
        imageView.setFitHeight(image.getHeight() * 0.3);

    }

    public void update(){
        if(target == null) {
            imageView.setImage(null);
            return;
        }

        if(face == CardFace.FRONT) {
            image = CardMuseumGUI.loadCardFront(target.id());
            imageView.setImage(image);
        }
        else {
            image = CardMuseumGUI.loadCardBack(target.id());
            imageView.setImage(image);
        }

    }

    public void setTarget(LightCard target) {
        this.target = target;
        this.update();
    }

    public void setPosition(double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
    }

    public void setTargetTranslation(double x, double y) {
        if(translationThread == null) {
            translationThread = new Thread(() -> {
                while(true) {
                    if(target != null) {
                        synchronized (lock) {
                            while (!farFromTarget.get()) {
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            double dx = tx - imageView.getTranslateX();
                            double dy = ty - imageView.getTranslateY();
                            double dist = dx * dx + dy * dy;
                            if (dist > 1) {
                                imageView.setTranslateX(imageView.getTranslateX() + dx / 10 );
                                imageView.setTranslateY(imageView.getTranslateY() + dy / 10 );
                            } else {
                                farFromTarget.set(false);
                            }
                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                    }
                }
            }
            }
            );
            translationThread.start();
        }
        synchronized (lock) {
            tx = x;
            ty = y;
            farFromTarget.set(true);
            lock.notifyAll();
        }

    }

    public ImageView getImageView() {
        return imageView;
    }

    public LightCard getTarget() {
        return target;
    }

}
