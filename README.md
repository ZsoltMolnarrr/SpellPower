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

These enchantments require the equipment to have at least some Spell Power attributes (such as + 1 Fire Spell Power).

In case of school limited enchantments the atttribute present must be relevant (for example Sunfire requires Arcane or Fire spell power to be present). 

(All enchantments are fully configurable, and come with descriptions)

# 🔧 Configuration

**Server side** configuration can be found in the `config` directory, after running the game with the mod installed.

# 🔨 Using it as a developer

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

## Using attributes

Use the attributes by directly referencing their original instance. For example: 
```java
// ✅ 
EntityAttributes_SpellPower.POWER.get(MagicSchool.FIRE);
EntityAttributes_SpellPower.CRITICAL_CHANCE;
```

Do not resolve them attribute registry, as they are not guaranteed to be registered at the runtime of your code.

```java
// 🚫
Registries.ATTRIBUTE.get(new Identifier("spell_power:fire"));
Registries.ATTRIBUTE.get(new Identifier("spell_power:critical_chance"));
```

### Query Spell Power

Use the dedicated API (`SpellPower` class) to query spell power of an entity (only PlayerEntities are supported). This will produce a result with critical strike support, and will take into account:
- the queried attribute
- critical strike related attributes (chance and multiplier)
- status effects 
- enchantments

```java
// Given `player` is a PlayerEntity
// ✅
var result = SpellPower.getSpellPower(player, MagicSchool.FIRE);
var value = result.randomValue(); // Randomly produces a critical strike or a base value (based on attributes)
var forcedCritValue = result.forcedCriticalValue(); // Forces a critical strike value
var forcedBaseValue = result.nonCriticalValue()(); // Forces a non-critical strike value
```

Do not use vanilla API to query Spell Power values, as it doesn't take into account any of the above mentioned factors.
```java
// 🚫
player.getAttributeValue(EntityAttributes_SpellPower.POWER.get(MagicSchool.FIRE));
```

### Adding attributes modifiers to equipment

Add attributes modifiers to your equipment items, to increase spell power of the player. For example:
```java
// Given: `ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder`
var fireSpellPower = 1;
builder.put(
    EntityAttributes_SpellPower.POWER.get(MagicSchool.FIRE),
    new EntityAttributeModifier(
        SomeUUID,
        "Spell Power",
        fireSpellPower,
        EntityAttributeModifier.Operation.ADDITION
    )
);
```