package cn.zefre.base.validation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * IPV4校验
 *
 * @author pujian
 * @date 2021/11/1 15:18
 */
public class IPV4Validator implements ConstraintValidator<IPV4, String> {

    private Pattern pattern;

    @Override
    public void initialize(IPV4 ipv4) {
        pattern = Pattern.compile(ipv4.regex());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (null == value) {
            return true;
        }
        return pattern.matcher(value).matches();
    }
}
