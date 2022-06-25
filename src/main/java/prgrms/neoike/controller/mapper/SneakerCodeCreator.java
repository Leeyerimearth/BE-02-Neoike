package prgrms.neoike.controller.mapper;

import lombok.NoArgsConstructor;
import prgrms.neoike.domain.sneaker.SneakerCategory;

import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@NoArgsConstructor(access = PRIVATE)
public class SneakerCodeCreator {

    public static String createSneakerCode(SneakerCategory sneakerCategory) {
        String preFix = sneakerCategory.name().substring(0, 2);
        String postFix = randomNumeric(7);

        return format("{0}-{1}", preFix, postFix);
    }
}