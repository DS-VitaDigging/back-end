package com.example.VitaDigging.service;


import com.example.VitaDigging.dto.ProductDto;
import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


@Service
@RequiredArgsConstructor
public class ProductApiService {

    private final ProductRepository productRepository;

    private final String API_KEY = "c5ad3115aa7b4c81a874";  // ⛔ 반드시 본인 키로 바꿔주세요
    private final String API_URL = "https://apis.data.go.kr/1471000/FoodNtrItdntInfoService01/getFoodNtrItdntList";

    public void fetchAndSaveProducts() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://openapi.foodsafetykorea.go.kr/api/" + API_KEY +
                "/I0030/json/1/50";

        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode items = root.path("I0030").path("row");

        for (JsonNode item : items) {

            String name = item.path("PRDLST_NM").asText();
            // 🔒 이미 존재하는 제품은 건너뜀
            if (productRepository.existsByName(name)) {
                continue;
            }

            ProductDto dto = new ProductDto();
            dto.setName(name);
            dto.setEfficacy(item.path("PRIMARY_FNCLTY").asText());
            dto.setIngredients(item.path("RAWMTRL_NM").asText());
            dto.setInstructions(item.path("NTK_MTHD").asText());
            dto.setPrecautions(item.path("IFTKN_ATNT_MATR_CN").asText());
            dto.setManufacturer(item.path("BSSH_NM").asText());


            // 🔄 카테고리 매핑 예시
            dto.setCategory(mapCategoryByFunction(dto.getEfficacy()));

            // 🔗 구매 링크 생성 (네이버 검색 링크)
            String encoded = URLEncoder.encode(dto.getName(), StandardCharsets.UTF_8);
            dto.setPurchaseLink("https://search.shopping.naver.com/search/all?query=" + encoded);

            // 임시 기본 이미지 URL 설정
            dto.setImageUrl("https://your-default-thumbnail.com/noimage.jpg");

            // Product 저장
            Product product = Product.builder()
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .efficacy(dto.getEfficacy())
                    .ingredients(dto.getIngredients())
                    .instructions(dto.getInstructions())
                    .precautions(dto.getPrecautions())
                    .manufacturer(dto.getManufacturer())
                    .purchaseLink(dto.getPurchaseLink())
                    .imageUrl(dto.getImageUrl())  // 🖼️ 수동 이미지 삽입
                    .build();

            productRepository.save(product);
        }
    }

    // 기능성 키워드를 바탕으로 카테고리 분류 (확장 버전)
    private String mapCategoryByFunction(String efficacy) {
        if (efficacy == null) return "기타";

        if (efficacy.contains("눈") || efficacy.contains("시력") || efficacy.contains("황반") || efficacy.contains("눈 피로"))
            return "눈 건강";

        if (efficacy.contains("면역") || efficacy.contains("피로") || efficacy.contains("에너지") || efficacy.contains("활력") || efficacy.contains("회복"))
            return "체력 증진";

        if (efficacy.contains("혈액") || efficacy.contains("혈행") || efficacy.contains("콜레스테롤") || efficacy.contains("중성지방") || efficacy.contains("혈압"))
            return "혈액 순환";

        if (efficacy.contains("피부") || efficacy.contains("미백") || efficacy.contains("주름") || efficacy.contains("항산화"))
            return "피부 건강";

        if (efficacy.contains("뼈") || efficacy.contains("칼슘") || efficacy.contains("골다공증") || efficacy.contains("관절"))
            return "뼈 강화";

        if (efficacy.contains("장") || efficacy.contains("배변") || efficacy.contains("유산균") || efficacy.contains("소화") || efficacy.contains("프로바이오틱스"))
            return "장 건강";

        return "기타";
    }

}