package com.dimitri.remoiville.go4lunch.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlaceDetailsPOJO {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public PlaceDetailsPOJO() {
    }

    /**
     *
     * @param result
     * @param htmlAttributions
     * @param status
     */
    public PlaceDetailsPOJO(List<Object> htmlAttributions, Result result, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class AddressComponent {

        @SerializedName("long_name")
        @Expose
        private String longName;
        @SerializedName("short_name")
        @Expose
        private String shortName;
        @SerializedName("types")
        @Expose
        private List<String> types = null;

        /**
         * No args constructor for use in serialization
         *
         */
        public AddressComponent() {
        }

        /**
         *
         * @param types
         * @param shortName
         * @param longName
         */
        public AddressComponent(String longName, String shortName, List<String> types) {
            super();
            this.longName = longName;
            this.shortName = shortName;
            this.types = types;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

    public static class Geometry {

        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("viewport")
        @Expose
        private Viewport viewport;

        /**
         * No args constructor for use in serialization
         *
         */
        public Geometry() {
        }

        /**
         *
         * @param viewport
         * @param location
         */
        public Geometry(Location location, Viewport viewport) {
            super();
            this.location = location;
            this.viewport = viewport;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

    }

    public static class Location {

        @SerializedName("lat")
        @Expose
        private Float lat;
        @SerializedName("lng")
        @Expose
        private Float lng;

        /**
         * No args constructor for use in serialization
         *
         */
        public Location() {
        }

        /**
         *
         * @param lng
         * @param lat
         */
        public Location(Float lat, Float lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLng() {
            return lng;
        }

        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public static class Northeast {

        @SerializedName("lat")
        @Expose
        private Float lat;
        @SerializedName("lng")
        @Expose
        private Float lng;

        /**
         * No args constructor for use in serialization
         *
         */
        public Northeast() {
        }

        /**
         *
         * @param lng
         * @param lat
         */
        public Northeast(Float lat, Float lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLng() {
            return lng;
        }

        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public static class Photo {

        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("html_attributions")
        @Expose
        private List<String> htmlAttributions = null;
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;
        @SerializedName("width")
        @Expose
        private Integer width;

        /**
         * No args constructor for use in serialization
         *
         */
        public Photo() {
        }

        /**
         *
         * @param htmlAttributions
         * @param photoReference
         * @param width
         * @param height
         */
        public Photo(Integer height, List<String> htmlAttributions, String photoReference, Integer width) {
            super();
            this.height = height;
            this.htmlAttributions = htmlAttributions;
            this.photoReference = photoReference;
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public List<String> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<String> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }

    public static class PlusCode {

        @SerializedName("compound_code")
        @Expose
        private String compoundCode;
        @SerializedName("global_code")
        @Expose
        private String globalCode;

        /**
         * No args constructor for use in serialization
         *
         */
        public PlusCode() {
        }

        /**
         *
         * @param globalCode
         * @param compoundCode
         */
        public PlusCode(String compoundCode, String globalCode) {
            super();
            this.compoundCode = compoundCode;
            this.globalCode = globalCode;
        }

        public String getCompoundCode() {
            return compoundCode;
        }

        public void setCompoundCode(String compoundCode) {
            this.compoundCode = compoundCode;
        }

        public String getGlobalCode() {
            return globalCode;
        }

        public void setGlobalCode(String globalCode) {
            this.globalCode = globalCode;
        }

    }

    public static class Result {

        @SerializedName("address_components")
        @Expose
        private List<AddressComponent> addressComponents = null;
        @SerializedName("adr_address")
        @Expose
        private String adrAddress;
        @SerializedName("business_status")
        @Expose
        private String businessStatus;
        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("formatted_phone_number")
        @Expose
        private String formattedPhoneNumber;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("international_phone_number")
        @Expose
        private String internationalPhoneNumber;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photos")
        @Expose
        private List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("plus_code")
        @Expose
        private PlusCode plusCode;
        @SerializedName("rating")
        @Expose
        private Float rating;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;
        @SerializedName("types")
        @Expose
        private List<String> types = null;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("user_ratings_total")
        @Expose
        private Integer userRatingsTotal;
        @SerializedName("utc_offset")
        @Expose
        private Integer utcOffset;
        @SerializedName("vicinity")
        @Expose
        private String vicinity;
        @SerializedName("website")
        @Expose
        private String website;

        /**
         * No args constructor for use in serialization
         *
         */
        public Result() {
        }

        /**
         *
         * @param adrAddress
         * @param formattedPhoneNumber
         * @param types
         * @param website
         * @param utcOffset
         * @param plusCode
         * @param icon
         * @param placeId
         * @param rating
         * @param userRatingsTotal
         * @param businessStatus
         * @param photos
         * @param url
         * @param reference
         * @param formattedAddress
         * @param reviews
         * @param addressComponents
         * @param name
         * @param geometry
         * @param vicinity
         * @param internationalPhoneNumber
         */
        public Result(List<AddressComponent> addressComponents, String adrAddress, String businessStatus, String formattedAddress, String formattedPhoneNumber, Geometry geometry, String icon, String internationalPhoneNumber, String name, List<Photo> photos, String placeId, PlusCode plusCode, Float rating, String reference, List<Review> reviews, List<String> types, String url, Integer userRatingsTotal, Integer utcOffset, String vicinity, String website) {
            super();
            this.addressComponents = addressComponents;
            this.adrAddress = adrAddress;
            this.businessStatus = businessStatus;
            this.formattedAddress = formattedAddress;
            this.formattedPhoneNumber = formattedPhoneNumber;
            this.geometry = geometry;
            this.icon = icon;
            this.internationalPhoneNumber = internationalPhoneNumber;
            this.name = name;
            this.photos = photos;
            this.placeId = placeId;
            this.plusCode = plusCode;
            this.rating = rating;
            this.reference = reference;
            this.reviews = reviews;
            this.types = types;
            this.url = url;
            this.userRatingsTotal = userRatingsTotal;
            this.utcOffset = utcOffset;
            this.vicinity = vicinity;
            this.website = website;
        }

        public List<AddressComponent> getAddressComponents() {
            return addressComponents;
        }

        public void setAddressComponents(List<AddressComponent> addressComponents) {
            this.addressComponents = addressComponents;
        }

        public String getAdrAddress() {
            return adrAddress;
        }

        public void setAdrAddress(String adrAddress) {
            this.adrAddress = adrAddress;
        }

        public String getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(String businessStatus) {
            this.businessStatus = businessStatus;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }

        public String getFormattedPhoneNumber() {
            return formattedPhoneNumber;
        }

        public void setFormattedPhoneNumber(String formattedPhoneNumber) {
            this.formattedPhoneNumber = formattedPhoneNumber;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getInternationalPhoneNumber() {
            return internationalPhoneNumber;
        }

        public void setInternationalPhoneNumber(String internationalPhoneNumber) {
            this.internationalPhoneNumber = internationalPhoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public PlusCode getPlusCode() {
            return plusCode;
        }

        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }

        public Float getRating() {
            return rating;
        }

        public void setRating(Float rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getUserRatingsTotal() {
            return userRatingsTotal;
        }

        public void setUserRatingsTotal(Integer userRatingsTotal) {
            this.userRatingsTotal = userRatingsTotal;
        }

        public Integer getUtcOffset() {
            return utcOffset;
        }

        public void setUtcOffset(Integer utcOffset) {
            this.utcOffset = utcOffset;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

    }

    public static class Review {

        @SerializedName("author_name")
        @Expose
        private String authorName;
        @SerializedName("author_url")
        @Expose
        private String authorUrl;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("profile_photo_url")
        @Expose
        private String profilePhotoUrl;
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("relative_time_description")
        @Expose
        private String relativeTimeDescription;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("time")
        @Expose
        private Integer time;

        /**
         * No args constructor for use in serialization
         *
         */
        public Review() {
        }

        /**
         *
         * @param authorName
         * @param authorUrl
         * @param rating
         * @param relativeTimeDescription
         * @param language
         * @param text
         * @param time
         * @param profilePhotoUrl
         */
        public Review(String authorName, String authorUrl, String language, String profilePhotoUrl, Integer rating, String relativeTimeDescription, String text, Integer time) {
            super();
            this.authorName = authorName;
            this.authorUrl = authorUrl;
            this.language = language;
            this.profilePhotoUrl = profilePhotoUrl;
            this.rating = rating;
            this.relativeTimeDescription = relativeTimeDescription;
            this.text = text;
            this.time = time;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorUrl() {
            return authorUrl;
        }

        public void setAuthorUrl(String authorUrl) {
            this.authorUrl = authorUrl;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getProfilePhotoUrl() {
            return profilePhotoUrl;
        }

        public void setProfilePhotoUrl(String profilePhotoUrl) {
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getRelativeTimeDescription() {
            return relativeTimeDescription;
        }

        public void setRelativeTimeDescription(String relativeTimeDescription) {
            this.relativeTimeDescription = relativeTimeDescription;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

    }

    public static class Southwest {

        @SerializedName("lat")
        @Expose
        private Float lat;
        @SerializedName("lng")
        @Expose
        private Float lng;

        /**
         * No args constructor for use in serialization
         *
         */
        public Southwest() {
        }

        /**
         *
         * @param lng
         * @param lat
         */
        public Southwest(Float lat, Float lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLng() {
            return lng;
        }

        public void setLng(Float lng) {
            this.lng = lng;
        }

    }

    public static class Viewport {

        @SerializedName("northeast")
        @Expose
        private Northeast northeast;
        @SerializedName("southwest")
        @Expose
        private Southwest southwest;

        /**
         * No args constructor for use in serialization
         *
         */
        public Viewport() {
        }

        /**
         *
         * @param southwest
         * @param northeast
         */
        public Viewport(Northeast northeast, Southwest southwest) {
            super();
            this.northeast = northeast;
            this.southwest = southwest;
        }

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }


}
