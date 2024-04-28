package it.polimi.ingsw.view.TUI.Styles;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.SpecialCollectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;

import java.util.Map;

public class CardTextStyle {
    private static final int CardWidth = 9;
    private static final int CardHeight = 5;

    public static final String ESCReset = "\u001B[0m";

    private static final Map<Collectable, String> collectableEmoji = Map.of(
            Resource.ANIMAL, new DecoratedString("🐺", StringStyle.LIGHT_BG).toString(), // 🐺
            Resource.INSECT, new DecoratedString("\uD83E\uDD8B", StringStyle.LIGHT_BG).toString(), //🦋
            Resource.FUNGI, new DecoratedString("\uD83C\uDF44", StringStyle.LIGHT_BG).toString(), //🍄
            Resource.PLANT, new DecoratedString("🌿", StringStyle.LIGHT_BG).toString(), //🌿
            WritingMaterial.INKWELL, new DecoratedString("\uD83E\uDED9", StringStyle.LIGHT_BG).toString(), //🫙
            WritingMaterial.QUILL, new DecoratedString("\uD83E\uDEB6", StringStyle.LIGHT_BG).toString(), //🪶
            WritingMaterial.MANUSCRIPT, new DecoratedString("\uD83D\uDCDC", StringStyle.LIGHT_BG).toString(), //📜
            SpecialCollectable.EMPTY, new DecoratedString("\uD83C\uDFB5", StringStyle.DARK_BG).toString()  // "🎵"
    );

    private static final String backgroundEmoji = "▪\uFE0F";

    private static final String coveredCornerMultiplierEmoji = "\uD83D\uDD17"; //🔗

    private static final String cornerFiller = new DecoratedString("\uD83D\uDD38", StringStyle.DARK_BG).toString(); //🔸

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

    private static final String Border = "➕"; //➕

    private static final Map<Resource, String> resourceFillings = Map.of(
            Resource.ANIMAL,  new DecoratedString("\uD83D\uDD35", StringStyle.BLUE_BG).toString(), //🔵
            Resource.INSECT, new DecoratedString("\uD83D\uDFE3", StringStyle.PURPLE_BG).toString(), //🟣
            Resource.FUNGI, new DecoratedString("\uD83D\uDD34", StringStyle.RED_BG).toString(), //🔴
            Resource.PLANT, new DecoratedString("\uD83D\uDFE2", StringStyle.GREEN_BG).toString() //🟢
    );

    private static final Map<Resource, String> resourceBorder = Map.of(
            Resource.ANIMAL,  new DecoratedString(CardTextStyle.getBorder(), StringStyle.BLUE_BG).toString(),
            Resource.INSECT, new DecoratedString(CardTextStyle.getBorder(), StringStyle.PURPLE_BG).toString(),
            Resource.FUNGI, new DecoratedString(CardTextStyle.getBorder(), StringStyle.RED_BG).toString(),
            Resource.PLANT, new DecoratedString(CardTextStyle.getBorder(), StringStyle.GREEN_BG).toString()
    );

    private static String startFilling = new DecoratedString("\uD83D\uDFE8", StringStyle.GOLD_BG).toString(); //🟨

    private static String frontierFilling = "❔";

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

    public static String getCoveredCornerMultiplierEmoji() {
        return coveredCornerMultiplierEmoji;
    }

    public static String getCornerFiller() {
        return cornerFiller;
    }

    public static String getResourceBorder(Resource resource){
        return resourceBorder.get(resource);
    }

    public static String getFrontierFilling() {
        return frontierFilling;
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