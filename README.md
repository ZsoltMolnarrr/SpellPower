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
#### Spell Power
- Represents power of spells with a number (somewhat analogous to `minecraft:generic_attack_damage` attribute), which serves as base to calculate spell damage
- Attribute id (formula): `spell_power:SCHOOL` (for example: `spell_power:fire`)
- A separate attribute exists for each specific magic school
- `Base value = 0`

#### Spell critical chance
- Represents chance of critical striking with spells 
- Attribute id: `spell_power:critical_chance`
- `Base value = 100` (technically) (this means 0% critical chance)
- Example values: 120% = 20% critical chance, 200% = 100% critical chance
- All players have a `0.05, MULTIPLY_BASE` modifier by default, so the practical default value is `105` chance, making all spells `5%` critical chance by default

#### Spell critical damage 
- Represents how much damage is increased when critical striking wit a spell
- Attribute id: `spell_power:critical_damage`
- `Base value = 100` (technically)  (this means critical strikes don't do more damage than non-critical ones)
- All players have a `0.5, MULTIPLY_BASE` modifier by default, so the practical default value is 150, making all spells doe 1.5x damage on critical strike by default

#### Spell haste
- Represents the spell casting speed, (to be used to quicken spell casting or cooldowns by spell implementations)
- Attribute id: `spell_power:haste`
- `Base value = 100` (this means player casts spells at normal speed)
- Players have no modifiers by default
- Example values = 50 (50% faster spell casting), 200 (200% faster spell casting)

### Status Effects:
Each introduced attribute (mentioned above), has with a matching status effect to boost them.

The id of these matches the with the id of the boosted attribute (for example: `spell_power:fire`, `spell_power:critical_chance`)

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
double value = result.randomValue(); // Randomly produces a critical strike or a base value (based on attributes)
double forcedCritValue = result.forcedCriticalValue(); // Forces a critical strike value
dobule forcedBaseValue = result.nonCriticalValue()(); // Forces a non-critical strike value
```

The value received is an abstract number. Spell implementations should calculate with this value using an arbitrary formula. This typically means some linear scaling. For example:
- A quickly casted spell named _Scorch_ might apply a low multiplier. `var damage = result.randomValue() * 0.5;`
- A slowly casted spell named _Fireball_ might apply a higher multiplier. `var damage = result.randomValue() * 0.9F;`

The total value of Spell Power queried completely depends on the content mods.

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

**How big bonuses should be used?**

As long as the user experience is intended to be Vanilla friendly, it is recommended to keep your Spell Power bonuses roughly in the same ballpark as vanilla `attack_damage` attribute. For example:
- A staff might have + 4 Fire Spell Power

### Adopting Spell haste

To retrieve the Spell Haste value of a player, use the following API:
```java
// Given `player` is a PlayerEntity
double haste = SpellPower.getHaste(player);
```

This value represents a relative casting speed. For example:
- When players have no haste bonus (so default attribute value) it returns `1.0`
- When players have 50% haste bonus (so attribute value of 150) it returns `1.5`

Haste can be calculated with at arbitrary formula. But the typical recommendation is the following:
```java
// Given `myCooldownDuration` is a valid number that presrents the duration of the cooldown
float hasteAffectedCooldownDuration = hasteAffectedValue(caster, myCooldownDuration);
// Given `mySpellCastDuration` is a valid number that presrents the duration of the spell cast
float hasteAffectedSpellCastDuration = hasteAffectedValue(caster, mySpellCastDuration);

float hasteAffectedValue(PlayerEntity caster, float value) {
    var haste = (float) SpellPower.getHaste(caster);
    return value / haste;
}
```

### Vulnerabilities

Vulnerabilities are a way to increase spell damage taken by an entity. They can be attached to any arbitrary trait or object of an entity.

A vulnerability can modify the following for a specific entity:
- Total spell damage taken
- Critical strike chance against the entity
- Critical strike damage against the entity

This library implements Vulnerabilities as status effects by default.

The following example shows how _Frozen_ status effect increases critical strike chance against frozen the entity.
```java
// 1. Create a StatusEffect subclassing SpellVulnerabilityStatusEffect, or implementing `VulnerabilityEffect`
public class FrozenStatusEffect extends SpellVulnerabilityStatusEffect { ... }

// 2. Create your status effect instance and configure it
public static StatusEffect frozen = new FrozenStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff)
        .setVulnerability(MagicSchool.FROST, new SpellPower.Vulnerability(0, 1F, 0F));

// 3. Register your status effect as usual
```

To add a completely custom vulnerability mechanic, the following can be used:
```java
SpellPower.vulnerabilitySources.add(query -> {
    var target = query.entity();
    // My logic
    return List.of(...)
})
```