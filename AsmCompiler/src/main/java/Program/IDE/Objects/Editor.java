package Program.IDE.Objects;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Program.IDE.StaticClasses.StaticVariable.*;

/**
 * Created by Michal on 2017-02-05.
 */
public class Editor {
    private CodeArea codeArea;


    private static final String MNEMONIC_PATTERN = "\\b(" + String.join("|", MNEMONICS) + ")\\b";
    private static final String REGISTRY_PATTERN = "\\b(" + String.join("|", REGISTERS) + ")\\b";
    private static final String DIRECTIVE_PATTERN = "\\b(" + String.join("|", DIRECTIVE) + ")\\b";
    private static final String COMMENT_PATTERN = "//[^\n]*" ; //+ "|" + "/\\*(.|\\R)*?\\*/"
    private static final String LABEL_PATTERN = "\\w+(?=(\\:))";
//    private static final String REST_PATTERN = "(.*)"; // ?!" + String.join("|", REGISTERS) + "


    private static final Pattern PATTERN = Pattern.compile(
            "(?<COMMENT>" + COMMENT_PATTERN + ")"
            + "|(?<DIRECTIVE>" + DIRECTIVE_PATTERN + ")"
            + "|(?<LABEL>" + LABEL_PATTERN + ")"
            + "|(?<MNEMONIC>" + MNEMONIC_PATTERN + ")"
            + "|(?<REGISTRY>" + REGISTRY_PATTERN + ")"
//            + "|(?<REST>" + REST_PATTERN + ")"
    );

    public Editor() {
        codeArea = new CodeArea();
        codeArea.setId("codearea");
        codeArea.setWrapText(true);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change -> {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText().toLowerCase()));
                });
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("COMMENT") != null ? "comment" :
                    matcher.group("LABEL") != null ? "label_j" :
                    matcher.group("MNEMONIC") != null ? "mnemonic" :
                    matcher.group("REGISTRY") != null ? "registry" :
                    matcher.group("DIRECTIVE") != null ? "directive" :
                             null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }
}
