package it.polimi.ingsw.view.TUI.Renderables.Styles;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.SpecialCollectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;

import java.util.Map;

public class CardTextStyle {
    private static final int CardWidth = 9;
    private static final int CardHeight = 5;
    private static final Map<Collectable, String> collectableEmoji = Map.of(
            Resource.ANIMAL, "\uD83D\uDC3A", // 🐺
            Resource.INSECT, "\uD83E\uDD8B", //🦋
            Resource.FUNGI, "\uD83C\uDF44", //🍄
            Resource.PLANT, "🌿", //🌿
            WritingMaterial.INKWELL, "\uD83E\uDED9", //🫙
            WritingMaterial.QUILL, "\uD83E\uDEB6", //🪶
            WritingMaterial.MANUSCRIPT, "\uD83D\uDCDC", //📜
            SpecialCollectable.EMPTY, "◻\uFE0F" // "◻️"
    );

    private static final String backgroundEmoji = "◾";

    private static final Map<Integer, String> numberEmoji = Map.of(
            1, "1️⃣",
            2, "2️⃣",
            3, "3️⃣",
            4, "4️⃣",
            5, "5️⃣",
            6, "6️⃣",
            7, "7️⃣",
            8, "8️⃣",
            9, "9️⃣",
            0, "0️⃣"
    );

    private static final String Border = "➕";

    private static final Map<Resource, String> resourceFillings = Map.of(
            Resource.ANIMAL, "\uD83D\uDD35", //🔵
            Resource.INSECT, "\uD83D\uDFE3", //🟣
            Resource.FUNGI, "\uD83D\uDD34", //🔴
            Resource.PLANT, "\uD83D\uDFE2" //🟢
    );

    private static String startFilling = "\uD83D\uDFE1"; //🟡

    public static int getCardWidth() {
        return CardWidth;
    }

    public static int getCardHeight() {
        return CardHeight;
    }

    /**
     * Get the emoji associated with the collectable, if the collectable is null return the border emoji
     * @param collectable the collectable to get the emoji of
     * @return the emoji of the collectable
     */
    public static String getCollectableEmoji(Collectable collectable){
        if(collectable == null)
            return getBorder();
        return collectableEmoji.get(collectable);
    }

    public static String getBorder(){
        return Border;
    }

    public static String getResourceFilling(Resource resource){
        return resourceFillings.get(resource);
    }

    public static String getNumberEmoji(int number){
        return numberEmoji.get(number);
    }

    public static String getBackgroundEmoji(){
        return backgroundEmoji;
    }

    public static String getStartFilling() {
        return startFilling;
    }

}



/*
⬜⬜⬜⬜5️⃣⬜⬜⬜🐺
⬜⬜🟦🟦🪶🟦🟦⬜⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
⬜⬜🟦3️⃣🟦1️⃣🟦⬜⬜
⬜🐺⬜🟡🟦🔴⬜⬜⬜
⬜⬜⬜⬜⬜⬜⬜⬜⬜
◾◾◾◾◾◾◾◾◾
◾◾◾◾◾◾◾◾◾
◾◾◾◾◾◾◾◾◾

⬜⬜⬜⬜5️⃣⬜⬜⬜⬛
⬜🐺🟦🟦🪶🟦🟦⬜⬜
⬜🟦🟦3️⃣🟦1️⃣🟦🟦⬜
⬜⬜🟦🟡🟦🔴🟦⬜⬜
🐺⬜⬜⬜⬜⬜⬜⬜🐺


🐺⬜⬜⬜5️⃣⬜⬜⬜⬜
⬜⬜🟦🟦🪶🟦🟦🟦⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
⬜🟦🟦3️⃣🟦1️⃣🟦🟦⬜
⬜⬜🟦🟡🟦🔴🟦⬜⬜
🐺⬜⬜⬜⬜⬜⬜⬜🐺
 */