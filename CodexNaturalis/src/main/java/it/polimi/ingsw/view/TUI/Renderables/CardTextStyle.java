package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.*;

import java.util.Map;

public class CardTextStyle {
    private static final int CardWidth = 9;
    private static final int CardHeight = 6;
    private static final Map<Collectable, String> collectableEmoji = Map.of(
            Resource.ANIMAL, "\uD83D\uDC3A", // 🐺
            Resource.INSECT, "\uD83E\uDD8B", //🦋
            Resource.FUNGI, "\uD83C\uDF44", //🍄
            Resource.PLANT, "\uD83C\uDF40", //🍀
            WritingMaterial.INKWELL, "\uD83E\uDED9", //🫙
            WritingMaterial.QUILL, "\uD83E\uDEB6", //🪶
            WritingMaterial.MANUSCRIPT, "\uD83D\uDCDC", //📜
            SpecialCollectable.EMPTY, "⬜" // "⬜"
    );

    private static String backgroundEmoji = "◾";

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

    private static final String Border = "⬜";

    private static final Map<Resource, String> resourceFillings = Map.of(
            Resource.ANIMAL, "\uD83D\uDFE6", //🟦
            Resource.INSECT, "\uD83D\uDFEA", //🟪
            Resource.FUNGI, "\uD83D\uDFE5",  //🟥
            Resource.PLANT, "\uD83D\uDFE9" //🟩
    );

    public static int getCardWidth() {
        return CardWidth;
    }

    public static int getCardHeight() {
        return CardHeight;
    }

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

}



/*
🐺⬜⬜⬜5️⃣⬜⬜⬜🐺
⬜⬜🟦🟦🪶🟦🟦⬜⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
⬜🟦🟦3️⃣🟦1️⃣🟦🟦⬜
⬜⬜🟦🟡🟦🔴🟦⬜⬜
🐺⬜⬜⬜⬜⬜⬜⬜🐺
◾◾◾◾◾◾◾◾◾
◾◾◾◾◾◾◾◾◾
◾◾◾◾◾◾◾◾◾

🐺⬜⬜⬜5️⃣⬜⬜⬜⬛
⬜⬜🟦🟦🪶🟦🟦⬜⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
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