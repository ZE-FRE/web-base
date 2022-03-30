package cn.zefre.base.validation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号约束Validator
 *
 * @author pujian
 * @date 2021/10/19 15:49
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private Pattern pattern;

    @Override
    public void initialize(Phone phoneAnnotation) {
        String regexp = phoneAnnotation.regexp();
        this.pattern = Pattern.compile(regexp);

    }

    @Override
    public boolean isValid(String phoneNum, ConstraintValidatorContext constraintValidatorContext) {
        if (null == phoneNum) {
            return true;
        }
        return pattern.matcher(phoneNum).matches();
    }
}
