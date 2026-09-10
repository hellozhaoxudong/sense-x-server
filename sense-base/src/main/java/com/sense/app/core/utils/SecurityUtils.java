package com.sense.app.core.utils;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class SecurityUtils {

    // 前后端传输重要核心数据时的RSA加密 公私钥对，前端使用公钥加密，后端使用私钥解密
    public static String IMPORTANT_DATA_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs1XqvXa0Z8AdVTvQckyifviQeXKkPaj1BnfxpXC4krYTDuib9sb93csWpydbluewC1kEJu04NzGtvAP5sSl9F6Ukom//x8hkpicExcBZYmfaLZ+UWtU0r/z5qVeavvOeCSfLoJ4bzFMAoZw7i7KIqC0mz5/KlJUDjaeyH2PuBfhltTOUarbgRfRBevZXF5f3J6TIYJaz9xjvuu6WMOok8lk0DFhbnfZrgJ7sRTfH14qL1D3UubmpCADxqhsnGcCdwthLiwppfAl0JW6nuVLSyeyidVcselXlVowMQdCB7ngC685Adl0KMlMwS7lEZF5fI5K/p+Wp+1YEj0yuARzgvwIDAQAB";
    public static String IMPORTANT_DATA_RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCzVeq9drRnwB1VO9ByTKJ++JB5cqQ9qPUGd/GlcLiSthMO6Jv2xv3dyxanJ1uW57ALWQQm7Tg3Ma28A/mxKX0XpSSib//HyGSmJwTFwFliZ9otn5Ra1TSv/PmpV5q+854JJ8ugnhvMUwChnDuLsoioLSbPn8qUlQONp7IfY+4F+GW1M5RqtuBF9EF69lcXl/cnpMhglrP3GO+67pYw6iTyWTQMWFud9muAnuxFN8fXiovUPdS5uakIAPGqGycZwJ3C2EuLCml8CXQlbqe5UtLJ7KJ1Vyx6VeVWjAxB0IHueALrzkB2XQoyUzBLuURkXl8jkr+n5an7VgSPTK4BHOC/AgMBAAECggEAXbPfsl1KyAcOjJFzSbWXFibEPmDTRlB4roePYHU0S5wM+zY4/8lbrj7GOFKNPZ0TU+qQC9LZE6XGdD3Tgmj+LpOgP6urhvTC+jna+YSWb5951WrTNl6+Z9ITlRLsDSf3hCsh72uNdMZNxyN9zXFcjBsDc2q3i8kMkUB1BofNP5p5h9QGsU3SBO/trM0ZmLl6nnZ/P7Y2wX49GToHtoMsdgSGuGxajULFGNkBw5w07ZaySA5XW2rjICgQQKYzQPLd9UbUwYouLiqAwvjwo1yUBH6lY6pACVwEsQhJAG96Do38Q4HRRPVD/hMjRVtTHT86oeWwsre6ZttHi1nXjYS1QQKBgQDXnjIvLFp40f4AuUvrqjeeXxYvRUmtOxLcDsxsp84ZRvcAOfkJK49yBHWdzlxmTWUqmmy2MRL4GiaxFrfJ5uJS1sl8M9uxKbK+Om/xGarqOi4kobwsfH/BPVX1stQStBAgx8k6E0V/SdbzOpQ5vNK7oLxPgi6kR8WAXyAVMZ2h3wKBgQDU7Cn2t1sZ0v86Hq78B2saKFaikdYphbuzjNxG+NEGObvdI13XjVfgvCHuPH/qdM4Nx+LAhIXHHhQkp7RYN/Vk5mpBgOQ3aw6CFVsqDPArHp697F5dUS+PkqLmmECxqNNhwIODQpMuRU5dA/9aB7isx8Vw0xg9MPYmObAwmaNdIQKBgDFvke4C61NcQjKESYXk5w+mmX2JE1Vmf/J4aiJXbweYn7UrlY+mryLjJtxhngnOoO1drqMvKTw9MrXTkZv3FdkMafHSKgK6nsLCvzu2ONuvLzky4+QHP3tcxsKol7saEcCSTe7E0GBY27G0sptyUZa3SbWpEWyVEBFYvhBd6dhdAoGBAIdyPDef6CoDWSzFvBbDOVSIeKQuFYiQdBkB8FaNjZpiO6mvB9FJLq/dvc/pWI3zA+ksUIibROansbSnokfEQSyQjuNgdMz0I2SI5VdYp6cmcWDr7hN6w8CXTV1XWUAq31z9tCtZvHrgx7rQAi5pXwc6hdUB1BeI/+zNIdGoyo1BAoGBAKcnzgNszyOuR1ymYHa3hsC5P/QAY2VQsBCdWGEaYNpriVm282ztBhMtHDyF8KvLnjJy6/gKZ0wlomkglaN1f7fvGFL93F1Ad85Gci4k5i8j3g8lnAdFDwK5w9W+7956QTd1F2Qv3SoYgKZRgEQlWfl0Dhr7bIIYfEnvc3USoZ/E";

    // 存储密码数据时的RSA加密 公私钥对，存储使用公钥加密，使用时使用私钥解密
    public static String PASSWORD_DATA_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxt31V3/8J5M26JUtUrnP4suk8uMlEbTBpZtZw/R/MyV7Hnkgou1Q/z4XaIX5vtY2nFRMsl7vu+mQhjXHarcQ+oz5RcvzrBDJ6dfC93wKslobtWg8EzvCaFe6G8RI3mr7BxlmUc+ma/VnN3X1ADZs1eG/j0FgVXWnWBNXJUFedP1LzY7IhVoy3t2xaCYxcPKcZ2cKZfQ6kR35YW/xcZLaSVAx8v3XSawDOR/A20b4PMqwV3Hn9qkLbbQqBQJsl8F0QQ8cFWtemrwiX2iv/PGpCSrM/ZpmL0gFlhcFs5RxT8uCzwEwzF4MoAZgvvhBIkMp6FcpKVTSLD5YCAJgsSYTNQIDAQAB";
    public static String PASSWORD_DATA_RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDG3fVXf/wnkzbolS1Suc/iy6Ty4yURtMGlm1nD9H8zJXseeSCi7VD/Phdohfm+1jacVEyyXu+76ZCGNcdqtxD6jPlFy/OsEMnp18L3fAqyWhu1aDwTO8JoV7obxEjeavsHGWZRz6Zr9Wc3dfUANmzV4b+PQWBVdadYE1clQV50/UvNjsiFWjLe3bFoJjFw8pxnZwpl9DqRHflhb/FxktpJUDHy/ddJrAM5H8DbRvg8yrBXcef2qQtttCoFAmyXwXRBDxwVa16avCJfaK/88akJKsz9mmYvSAWWFwWzlHFPy4LPATDMXgygBmC++EEiQynoVykpVNIsPlgIAmCxJhM1AgMBAAECggEAA3sA/z4LMLof3OQ3fdoorER6J5IYJP4v4XEdmhNeYovGYtsl2hQjNcRlbnvhyhg/rm6/ROPqeDR3RhIM4m4/ytLKzhDqu9kzX3daPzCZk1OGZp9jZcbG+UKHzFKcH+Bpck9cfhQPSpZp7wuOdSl+sCOaUayIPhaoDh+Qh+Nj/tIkKcHMiJ7dyMannvVyzmwQ+OEMoCtrlL7OwTPAJWaBWB35IlmE6AkFEW1PgB0WHvUfHVk9Xd7HF4f4wknrwB/OB3BqR3hl1pmAL7iFwXCyJAiVW1YBUZf8/aRU1coIZ6fnxHJrsfNDhuWjDeNNXdf48qAi1ZK59taZ0AKv/drD4QKBgQDoTGw6/gCEBz7EYLfRToHlAM1IbQ9EfSiCJU6y6XTKkwnUEjGDr7nJV6g9ssiexn9AH3JpZaJ/BTkI4j6WpYkcqwjiVedlmL0H+wfZJrBwXHRn3H8DolDBNtlJcunbYVCQ3El5uxMLSoNMC0rUwDMnwQxYUDzuE8eJlVMQrEV8/QKBgQDbKFA9+qaX7vU2Umhm2/C9cGqNQBUAwRF56TaXxj4I4Jw9OF3KNxbObRmQrZuI/fDOhgknuH+Gwmmn4o61IIUV6dhPpXlp8WearW07R/s7ovQhvmA0cI1YGGrvyGVaaLg6aBL37u5GQ57EdY0A/uPc+GdIGM0AibgOXgomm5XgmQKBgQCKhxh7zYEtOszZevvjW7fmgqXorPDP3PtNMUZPNx0ouKrBxt+0MhTO1vFDadxtot27tYUbCTrwGIM8ov7EA49Sg0RXBPdrBLylh7PUcVgCiX/5d+pelmsl7bmCLX78DnpnCJbgewkOUK8fqBRF7yAScvMTrzXGnBEAIQZaCQGfWQKBgFBOinDbu1YrUyGvuuAOMj5IxlKyK8AV/2Br6/xbwXmhTL9GO2ARJcNIyThx1W+ylh4nkY1Dlz97WBoT8/U9UGdCRGiTG1WOB0fvyvoRG2e0A7+lsj0lDTY95FmvVix1+SQ15NWDcT7AIpqc9KlrvXXIdJg/tkc/L/nbzd5t3tLpAoGBALj013nrHapt2pyx679WIC1xKC318PIvmKotv4qkGY+36K4RfCaHwISShua7E1eHOf5n8QG1zyBtTmkS7Df8Bwzh17mQhdqIzvkkrg3NaeB6oTsp2w0UZ9YFTbT5CRbQw6Lh/PN/yosRxnnZ5EQiL56q+A+b/Rozi/aOKoS1+Se3";

    /**
     * 核心数据传输：使用公钥加密字符串
     * @param waitEncryptStr    等待加密的字符串
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String importantDataEncryptRsa(String waitEncryptStr){
        RSA rsa = SecureUtil.rsa(null, IMPORTANT_DATA_RSA_PUBLIC);
        String en = rsa.encryptHex(waitEncryptStr, KeyType.PublicKey);
        return en;
    }

    /**
     * 核心数据传输：使用私钥解密字符串
     * @param encryptStr    加密后的字符串，前端应使用IMPORTANT_DATA_RSA_PUBLIC对数据进行加密
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String importantDataDecryptRsa(String encryptStr){
        RSA rsa = SecureUtil.rsa(IMPORTANT_DATA_RSA_PRIVATE, null);
        String de = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
        return de;
    }


    /**
     * 密码数据存储：使用公钥加密字符串
     * @param waitEncryptStr    等待加密的字符串
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String passwordDataEncryptRsa(String waitEncryptStr){
        RSA rsa = SecureUtil.rsa(null, PASSWORD_DATA_RSA_PUBLIC);
        String en = rsa.encryptHex(waitEncryptStr, KeyType.PublicKey);
        return en;
    }

    /**
     * 密码数据存储：使用私钥解密字符串
     * @param encryptStr    加密后的字符串，存储应使用PASSWORD_DATA_RSA_PUBLIC对数据进行加密
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String passwordDataDecryptRsa(String encryptStr){
        RSA rsa = SecureUtil.rsa(PASSWORD_DATA_RSA_PRIVATE, null);
        String de = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
        return de;
    }
}
