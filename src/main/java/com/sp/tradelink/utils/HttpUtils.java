package com.sp.tradelink.utils;

public class HttpUtils {
//    public static String callAPIUsingWebClient(QuantumHBRequest info, String server, String endpoint) {
//        try {
////            SslContext sslContext = SslContextBuilder
////                    .forClient()
////                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
////                    .build();
////            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
////            WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
////                    .baseUrl(server).build();
//
//            WebClient client = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
//                    .baseUrl(server).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
//
//            final WebClient.RequestHeadersSpec<?> spec = client.post().uri(endpoint)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .body(Mono.just(info.toString()), String.class);
//
//            return Objects.requireNonNull(spec.retrieve().toEntity(String.class).block()).getBody();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return Objects.requireNonNull(Mono.empty().block()).toString();
//        }
//    }
//
//    public static String callAPIUsingTemplate(QuantumHBRequest info, String server, String endpoint) {
//        RestTemplate quantum = new RestTemplate();
//        String serverEndpoint = server + endpoint.substring(1);
//
//        HttpHeaders headers = new HttpHeaders();
//        List<MediaType> accept =
//                Collections.singletonList(MediaType.asMediaType(MediaType.APPLICATION_JSON));
//        headers.setAccept(accept);
//        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(MediaType.APPLICATION_JSON_VALUE)));
//        HttpEntity<String> entity = new HttpEntity<>(info.toString(), headers);
//        return quantum.exchange(serverEndpoint, HttpMethod.POST, entity, String.class).getBody();
//    }
}
