package com.example.boulderate;

import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Gym> mockGyms() {
        ArrayList<Gym> gyms = new ArrayList<>();

        gyms.add(new Gym()
                .setTitle("Vking")
                .setImage("data:image/png;base64,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")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("iclimb")
                .setImage("https://www.ligdol.co.il/Upload/%D7%90%D7%99%D7%99_%D7%A7%D7%9C%D7%99%D7%9E%D7%91_%D7%94%D7%A8%D7%A6%D7%9C%D7%99%D7%94.png")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Performance Rock")
                .setImage("https://vegan-friendly.co.il/img/logos/1637674381_thumb_1637674138_18129.png")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Isaac")
                .setImage("https://static.wixstatic.com/media/f5d47c_b7174511332749f8be4ca7de26780557~mv2.png/v1/crop/x_106,y_29,w_296,h_431/fill/w_194,h_282,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/1.png")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("The Block")
                .setImage("https://static.wixstatic.com/media/26571c_28e4391423b44ffeacab1fc19ee6f4a5~mv2.png/v1/fill/w_786,h_816,al_c,q_90,usm_0.66_1.00_0.01,enc_auto/224%20(1).png")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Venga")
                .setImage("https://yt3.googleusercontent.com/ytc/AGIKgqMG9ox6lxTqA4YKf_N1KnruRK5O05pnfWR5lz49=s900-c-k-c0x00ffffff-no-rj")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Monkeys")
                .setImage("https://static.wixstatic.com/media/dc0053_b1eb3c0b8ca44eee8ad7c6c2bfa24032~mv2_d_2953_1476_s_2.png/v1/fill/w_1268,h_634,al_c,q_90,usm_0.66_1.00_0.01,enc_auto/dc0053_b1eb3c0b8ca44eee8ad7c6c2bfa24032~mv2_d_2953_1476_s_2.png")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Rockiz")
                .setImage("https://static.wixstatic.com/media/c2a70e_e3ba5ed8054f4a7287afacbd71f4d42d~mv2.jpg/v1/fill/w_1200,h_724,al_c/c2a70e_e3ba5ed8054f4a7287afacbd71f4d42d~mv2.jpg")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Funtopia")
                .setImage("https://scontent.ftlv23-1.fna.fbcdn.net/v/t39.30808-6/301693274_509286031197770_1482768909145075766_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=oLkHPVk_d4IAX-ChpB5&_nc_ht=scontent.ftlv23-1.fna&oh=00_AfCFk4uo-9mh-oiNAOfLFhJ78wGs_cPeY8-b4Wte0tmsLw&oe=64AA0117")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        gyms.add(new Gym()
                .setTitle("Levin")
                .setImage("https://yt3.googleusercontent.com/7gCz2pmfuCxXMLZESPlTyotjGkDWXATrYfS5wJWgx_K_jfT9kPqqc40voUdNSLY4d_TN_ut2UOg=s900-c-k-c0x00ffffff-no-rj")
                .setRating(0.0)
                .setUsersRatings(new ArrayList<>())
        );

        return gyms;
    }
}
