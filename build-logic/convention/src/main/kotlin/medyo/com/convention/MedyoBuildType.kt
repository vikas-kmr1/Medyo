package medyo.com.core.utils.app

enum class MedyoBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".dev"),
    RELEASE,
}

enum class Flavor(val flavorName: String) {
    DEV("dev"),
    PROD("prod")
}