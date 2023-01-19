![Spell Power](.github/icon_and_title.png)


<div align="center">

<a href="">![Java 17](https://img.shields.io/badge/Java%2017-ee9258?logo=coffeescript&logoColor=ffffff&labelColor=606060&style=flat-square)</a>
<a href="">![Environment: Client & Server](https://img.shields.io/badge/environment-Client%20&%20Server-1976d2?style=flat-square)</a>
<a href="">[![Discord](https://img.shields.io/discord/973561601519149057.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.gg/KN9b3pjFTM)</a>

</div>

# 🔮️ Features

This library introduces new Entity Attributes for powering magical abilities, for the following magic types (schools):
- 🔮 Arcane
- 🔥 Fire
- ❄️ Frost
- 💚 Healing
- ⚡️ Lightning
- 👻 Soul

(Note: the design intent is to stay native to Minecraft, but establish Warcraft like magic schools. So no classic 4 element schools are implemented, besides fire.)

The library offers an API to query spell damage of an entity (based on its attributes, status effects, enchantments), and provides critical strike chance and multiplier. Critical striking is completely rng based, powered by secondary attributes.

## 📦 Content

### Entity Attributes:
- Spell Power, one for each specific magic school (for example: `spell_power:fire`)
- Spell critical chance (id: `spell_power:critical_chance`)
- Spell critical damage (id: `spell_power:critical_damage`)
- Spell haste (id: `spell_power:haste`), can be used to quicken spell casting or cooldowns

### Status Effects:
- One specifically for each introduced attribute, with a matching id (for example: `spell_power:fire`, `spell_power:critical_chance`)

(All status effects come with fancy icons 😍)

### Enchantments:
- Universal Spell Power (named: "Spell Power"), increasing all spell damage
- School limited Spell Power (for example: "Sunfire", increasing arcane and fire damage) 
- Secondary attribute enchantments (for example: "Spell Critical Chance")
- "Magic Protection" (totally symmetric to Projectile Protection, but for magic)

(All enchantments are fully configurable, and come with descriptions)

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
    modImplementation "maven.modrinth:spell-power:VERSION-fabric"
}
```
In `fabric.mod.json` add a dependency to the mod:
```json
  "depends": {
    "spell_power": ">=VERSION"
  },
```

(Substitute `VERSION` with the name of the latest release available on [Modrinth](https://modrinth.com/mod/spell-power/versions))

### Forge workspace
```groovy
dependencies {
    implementation "maven.modrinth:spell-power:VERSION-forge"
}
```
In `mods.toml` add a dependency to the mod:
```
modId="spell_power"
mandatory=true
versionRange="[VERSION,)"
ordering="AFTER"
side="BOTH"
```

(Substitute `VERSION` with the name of the latest release available on [Modrinth](https://modrinth.com/mod/spell-power/versions))

## Using attributes

### Magic enchants for armor pieces

To allow your armor pieces to be enchanted with magical enchants use `MagicArmorEnchanting.register(myArmorPiece);` at item registration.