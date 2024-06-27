package it.polimi.ingsw.view.TUI.Styles;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.SpecialCollectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.Map;

/**
 * This class contains the styles used to render the cards in the TUI.
 * All the methods and fields are static.
 */
public class CardTextStyle {
    /** The width of a card in #chars. */
    private static final int CardWidth = 9;
    /** The height of a card in #chars. */
    private static final int CardHeight = 5;

    /** A map that associates a pawn color to a foreground color style. */
    private static final Map<PawnColors, StringStyle> pawnToStyle = Map.of(
            PawnColors.BLUE, StringStyle.BLUE_FOREGROUND,
            PawnColors.RED, StringStyle.RED_FOREGROUND,
            PawnColors.GREEN, StringStyle.GREEN_FOREGROUND,
            PawnColors.YELLOW, StringStyle.GOLD_FOREGROUND
    );

    /** A map that associates a pawn color to a background color style. */
    private static final Map<PawnColors, StringStyle> pawnToBgStyle = Map.of(
            PawnColors.BLUE, StringStyle.BLUE_BG,
            PawnColors.RED, StringStyle.RED_BG,
            PawnColors.GREEN, StringStyle.GREEN_BG,
            PawnColors.YELLOW, StringStyle.GOLD_BG
    );

    /** A map that associates a collectable to an emoji. */
    private static final Map<Collectable, String> collectableEmoji = Map.of(
            Resource.ANIMAL, new DecoratedString("🐺", StringStyle.DARK_BG).toString(), // 🐺
            Resource.INSECT, new DecoratedString("\uD83E\uDD8B", StringStyle.DARK_BG).toString(), //🦋
            Resource.FUNGI, new DecoratedString("\uD83C\uDF44", StringStyle.DARK_BG).toString(), //🍄
            Resource.PLANT, new DecoratedString("🌿", StringStyle.DARK_BG).toString(), //🌿
            WritingMaterial.INKWELL, new DecoratedString("\uD83E\uDED9", StringStyle.DARK_BG).toString(), //🫙
            WritingMaterial.QUILL, new DecoratedString("\uD83E\uDEB6", StringStyle.DARK_BG).toString(), //🪶
            WritingMaterial.MANUSCRIPT, new DecoratedString("\uD83D\uDCDC", StringStyle.DARK_BG).toString(), //📜
            SpecialCollectable.EMPTY, new DecoratedString("\uD83C\uDFB5", StringStyle.DARK_BG).toString()  // "🎵"
    );

    /** The emoji used to fill the background of the Codex. It is meant to be transparent*/
    private static final String backgroundEmoji = "  ";

    /** The emoji used to represent a covered corner multiplier */
    private static final String coveredCornerMultiplierEmoji = new DecoratedString("\uD83D\uDD17", StringStyle.DARK_BG).toString(); //🔗

    /** The emoji used to represent the equality between multiplier and given points in an objective card */
    private static final String equalEmoji = new DecoratedString("✖\uFE0F", StringStyle.GOLD_BG).toString(); //✖️

    /** The emoji used to fill the corners of the card */
    private static final String cornerFiller = new DecoratedString("\uD83D\uDD38", StringStyle.DARK_BG).toString(); //🔸

    /** A map that associates a digit to an emoji */
    private static final Map<Integer, String> numberEmoji = Map.of(
            1, " 1",
            2, " 2",
            3, " 3",
            4, " 4",
            5, " 5",
            6, " 6",
            7, " 7",
            8, " 8",
            9, " 9",
            0, " 0"
    );

    /** The emoji used to represent the border of a card */
    private static final String Border = "➕"; //➕

    /** A map that associates a resource to an emoji */
    private static final Map<Resource, String> resourceFillings = Map.of(
            Resource.ANIMAL,  new DecoratedString("\uD83D\uDD35", StringStyle.BLUE_BG).toString(), //🔵
            Resource.INSECT, new DecoratedString("\uD83D\uDFE3", StringStyle.PURPLE_BG).toString(), //🟣
            Resource.FUNGI, new DecoratedString("\uD83D\uDD34", StringStyle.RED_BG).toString(), //🔴
            Resource.PLANT, new DecoratedString("\uD83D\uDFE2", StringStyle.GREEN_BG).toString() //🟢
    );

    /** A map that associates a resource to a border emoji */
    private static final Map<Resource, String> resourceBorder = Map.of(
            Resource.ANIMAL,  new DecoratedString(CardTextStyle.getBorder(), StringStyle.BLUE_BG).toString(),
            Resource.INSECT, new DecoratedString(CardTextStyle.getBorder(), StringStyle.PURPLE_BG).toString(),
            Resource.FUNGI, new DecoratedString(CardTextStyle.getBorder(), StringStyle.RED_BG).toString(),
            Resource.PLANT, new DecoratedString(CardTextStyle.getBorder(), StringStyle.GREEN_BG).toString()
    );

    /** The emoji used to fill the start card */
    private static final String startFilling = new DecoratedString("\uD83D\uDFE8", StringStyle.GOLD_BG).toString(); //🟨

    /** The emoji used to fill the frontier card */
    private static final String frontierFilling = "❔";

    /**
     * Get the width of a card in #chars
     * @return the width of a card
     */
    public static int getCardWidth() {
        return CardWidth;
    }

    /**
     * Get the height of a card in #chars
     * @return the height of a card
     */
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

    /**
     * Get the emoji used to fill the border of a card
     * @return the emoji used to fill the border of a card
     */
    public static String getBorder(){
        return Border;
    }

    /**
     * Get the emoji associated with the resource, if the resource is null return the border emoji
     * @param resource the resource to get the emoji of
     * @return the emoji of the resource
     */
    public static String getResourceFilling(Resource resource){
        return resourceFillings.get(resource);
    }

    /**
     * Get the emoji associated with the number
     * @param number the number to get the emoji of
     * @return the emoji of the number
     */
    public static String getNumberEmoji(int number){
        return new DecoratedString(numberEmoji.get(number), StringStyle.DARK_BG).toString();
    }

    /**
     * Gets the background emoji for the Codex
     * @return the background emoji for the Codex
     */
    public static String getBackgroundEmoji(){
        return backgroundEmoji;
    }

    /**
     * Get the emoji used to fill the start card
     * @return the emoji used to fill the start card

     */
    public static String getStartFilling() {
        return startFilling;
    }

    /**
     * Get the emoji used to fill the frontier card
     * @return the emoji used to fill the frontier card
     */
    public static String getCoveredCornerMultiplierEmoji() {
        return coveredCornerMultiplierEmoji;
    }

    /**
     * Get the emoji used to fill the corners of the card
     * @return the emoji used to fill the corners of the card
     */
    public static String getCornerFiller() {
        return cornerFiller;
    }

    /**
     * Gets the emoji used to fill the frontier card
     * @return the emoji used to fill the frontier card
     */
    public static String getFrontierFilling() {
        return frontierFilling;
    }

    /**
     * Get the emoji used to represent the equality between multiplier and given points in an objective card
     * @return the emoji used to represent the equality between multiplier and given points in an objective card
     */
    public static String getEqualEmoji() {
        return equalEmoji;
    }

    /**
     * Get the color style associated with the pawn color
     * @param pawnColor the pawn color to get the style of
     * @return the style associated with the pawn color
     */
    public static StringStyle convertPawnColor(PawnColors pawnColor){
        return pawnToStyle.get(pawnColor);
    }

    /**
     * Get the background color style associated with the pawn color
     * @param pawnColor the pawn color to get the style of
     * @return the style associated with the pawn color
     */
    public static StringStyle convertPawnBgColor(PawnColors pawnColor){
        return pawnToBgStyle.get(pawnColor);
    }

    /**
     * The max width of the codex in #chars
     */
    public static int codexMaxWidth = 200;
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

aaaaaaaaaaaaaaaaaaaaaa
🐺⬜⬜⬜5️⃣⬜⬜⬜⬜
⬜⬜🟦🟦🪶🟦🟦🟦⬜
⬜🟦🟦🟦🟦🟦🟦🟦⬜
⬜🟦🟦3️⃣🟦1️⃣🟦🟦⬜
⬜⬜🟦🟡🟦🔴🟦⬜⬜
🐺⬜⬜⬜⬜⬜⬜⬜🐺
 */