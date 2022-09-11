<div align="center">

<a href="">![Java 17](https://img.shields.io/badge/Java%2017-ee9258?logo=coffeescript&logoColor=ffffff&labelColor=606060&style=flat-square)</a>
<a href="">![Environment: Client & Server](https://img.shields.io/badge/environment-Client%20&%20Server-1976d2?style=flat-square)</a>
<a href="">[![Discord](https://img.shields.io/discord/973561601519149057.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.gg/KN9b3pjFTM)</a>

</div>

# 🪄 Features

# 🔨 Using it as a modder

Add this mod as dependency into your build.gradle file.
```groovy
repositories {
    maven {
        name = 'Modrinth'
        url = 'https://api.modrinth.com/maven'
        content {
            includeGroup 'maven.modrinth'
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:spell-damage-attribute:VERSION"
}
```

(Substitute `VERSION` with the name of the latest release available on [Modrinth](https://modrinth.com/mod/spell-damage-attribute/versions), for example: `1.0.8+1.19`)

In `fabric.mod.json` add a dependency to the mod:
```json
  "depends": {
    "projectiledamage": ">=VERSION"
  },
```

(Substitute `VERSION` with the latest release version of this mod, for example: `1.0.8+1.19`)

Make sure the inheritance chain of your custom ranged weapon includes `RangedWeaponItem` or provide a custom implementation of `net.projectiledamage.api.IProjectileWeapon` interface (default implementaion can be found [here](./src/main/java/net/projectiledamage/api/IProjectileWeapon.java)).

Set the spell damage for your weapon instance, preferably before registering it:
```java
var myFancyBow = MyFancyBow();
((IProjectileWeapon)myFancyBow).setProjectileDamage(7);
```
