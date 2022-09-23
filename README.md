<div align="center">

<a href="">![Java 17](https://img.shields.io/badge/Java%2017-ee9258?logo=coffeescript&logoColor=ffffff&labelColor=606060&style=flat-square)</a>
<a href="">![Environment: Client & Server](https://img.shields.io/badge/environment-Client%20&%20Server-1976d2?style=flat-square)</a>
<a href="">[![Discord](https://img.shields.io/discord/973561601519149057.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.gg/KN9b3pjFTM)</a>

</div>

# 🔮️ Features

Adds new entity attributes:
- Multiple kind of Spell damage
- Spell critical chance
- Spell critical damage
- Spell haste

Adds new enchantments:
- Spell Power

Adds new status effects:
- ...

# 🔧 Configuration

**Server side** configuration can be found in the `config` directory, after running the game with the mod installed.

# 🔨 Using it as a modder

## Installation

Add this mod as dependency into your build.gradle file.

Repository
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
```

### Fabric workspace
```groovy
dependencies {
    modImplementation "maven.modrinth:spell-damage-attribute:VERSION-fabric"
}
```
In `fabric.mod.json` add a dependency to the mod:
```json
  "depends": {
    "spelldamage": ">=VERSION"
  },
```

(Substitute `VERSION` with the name of the latest release available on [Modrinth](https://modrinth.com/mod/spell-damage-attribute/versions))

### Forge workspace
```groovy
dependencies {
    implementation "maven.modrinth:spell-damage-attribute:VERSION-forge"
}
```
In `mods.toml` add a dependency to the mod:
```
modId="spelldamage"
mandatory=true
versionRange="[VERSION,)"
ordering="AFTER"
side="BOTH"
```

(Substitute `VERSION` with the name of the latest release available on [Modrinth](https://modrinth.com/mod/spell-damage-attribute/versions))
