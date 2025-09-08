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
- and the ones you want to add via Java API :)

(Note: the design intent is to stay native to Minecraft, but establish Warcraft like magic schools. So no classic 4 element schools are implemented, besides fire.)

The library offers an API to query spell power of an entity (based on its attributes, status effects, enchantments), and provides critical strike chance and multiplier. Critical striking is completely rng based, powered by secondary attributes.

## 📦 Content

### Entity Attributes
#### Spell Power
- Represents power of spells with a number (somewhat analogous to `minecraft:generic_attack_damage` attribute), which serves as base to calculate spell damage
- Attribute id (formula): `spell_power:SCHOOL` (for example: `spell_power:fire`)
- A separate attribute exists for each specific magic school
- `Base value = 0`

#### Spell critical chance
- Represents chance of critical striking with spells 
- Attribute id: `spell_power:critical_chance`
- `Base value = 100` (technically) (this means 0% critical chance)
- Example values: `120` ( = 20% critical chance), `200` ( = 100% critical chance)
- All players have a `0.05, MULTIPLY_BASE` modifier by default, so the practical default value is `105` chance, making all spells `5%` critical chance by default

#### Spell critical damage 
- Represents how much damage is increased when critical striking wit a spell
- Attribute id: `spell_power:critical_damage`
- `Base value = 100` (this means critical strikes don't do more damage than non-critical ones)
- Example values: `150` ( = 150% critical damage), `200` ( = 200% critical chance)
- All players have a `0.5, MULTIPLY_BASE` modifier by default, so the practical default value is 150, making all spells doe 1.5x damage on critical strike by default

#### Spell haste
- Represents the spell casting speed, (to be used to quicken spell casting or cooldowns by spell implementations)
- Attribute id: `spell_power:haste`
- `Base value = 100` (this means player casts spells at normal speed)
- Players have no modifiers by default
- Example values: `150` = 50% faster spell casting, `200` = 100% faster spell casting

#### Resistance
- Represents resistance against spell damage (magical analog to armor)
- Attribute id: `spell_power:resistance.generic` (protects against all magic schools)
- Calculation is configurable, with the following formulas available:
  - `LINEAR` : `r / C`
  - `QUADRATIC` : `sqrt(r * C) / C`
  - `HYPERBOLIC` : `r / (r + C)` (default)
  - Where `r` is the resistance attribute value, and `C` is a configurable tuning constant (default: `20`)

### Status Effects
Each introduced attribute (mentioned above), has with a matching status effect to boost them.

The id of these matches the with the id of the boosted attribute (for example: `spell_power:fire`, `spell_power:critical_chance`)

(All status effects come with fancy icons 😍)

### Enchantments
- Universal Spell Power (named: "Spell Power"), increasing all spell damage
- School limited Spell Power (for example: "Sunfire", increasing arcane and fire damage) 
- Secondary attribute enchantments (for example: "Spell Critical Chance")
- "Magic Protection" (totally symmetric to Projectile Protection, but for magic)

These enchantments require the equipment to have at least some Spell Power attributes (such as + 1 Fire Spell Power).

In case of school limited enchantments the atttribute present must be relevant (for example Sunfire requires Arcane or Fire spell power to be present). 

(All enchantments are fully configurable, and come with descriptions)

To enable applying these enchantments to any item, add the item the one or more of the following tags:
- `spell_power:enchantable/critical_damage`
- `spell_power:enchantable/critical_chance`
- `spell_power:enchantable/haste`
- `spell_power:enchantable/spell_power_energize`
- `spell_power:enchantable/spell_power_generic`
- `spell_power:enchantable/spell_power_soulfrost`
- `spell_power:enchantable/spell_power_specialized`
- `spell_power:enchantable/spell_power_sunfire`

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
    modImplementation("maven.modrinth:spell-power:${project.spell_power_version}")
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
SpellSchools.FIRE.attribute;
SpellPowerMechanics.CRITICAL_CHANCE.attribute;
SpellPowerMechanics.CRITICAL_DAMAGE.attribute;
SpellPowerMechanics.HASTE.attribute;
```

Alternatively you can resolve them from attribute registry.

(Note: power attributes of schools from third party developers may not be found this way, depending on which point they perform the registration.) 

```java
// ✅
Registries.ATTRIBUTE.get(Identifier.of("spell_power:fire"));
Registries.ATTRIBUTE.get(Identifier.of("spell_power:critical_chance"));
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
var result = SpellPower.getSpellPower(player, SpellSchools.FIRE);
double value = result.randomValue(); // Randomly produces a critical strike or a base value (based on attributes)
double forcedCritValue = result.forcedCriticalValue(); // Forces a critical strike value
dobule forcedBaseValue = result.nonCriticalValue(); // Forces a non-critical strike value
```

The value received is an abstract number. Spell implementations should calculate with this value using an arbitrary formula. This typically means some linear scaling. For example:
- A quickly casted spell named _Scorch_ might apply a low multiplier. `var damage = result.randomValue() * 0.5;`
- A slowly casted spell named _Fireball_ might apply a higher multiplier. `var damage = result.randomValue() * 0.9F;`

The total value of Spell Power queried completely depends on the content mods.

Do not use vanilla API to query Spell Power values, as it doesn't take into account any of the above mentioned factors.
```java
// 🚫
player.getAttributeValue(SpellSchools.FIRE.attribute);
```

### Adding attributes modifiers to equipment

Add attributes modifiers to your equipment items, to increase spell power of the player. For example:
```java
// Given: `ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder`
var fireSpellPower = 1; // + 1 Fire Spell Power
builder.put(SpellSchools.FIRE.attribute,
    new EntityAttributeModifier(
        SomeUUID,
        "Spell Power",
        fireSpellPower,
        EntityAttributeModifier.Operation.ADDITION
    )
);

var haste = 0.1; // +10% Spell Haste
builder.put(SpellPowerMechanics.HASTE.attribute,
    new EntityAttributeModifier(
        SomeUUID,
        "Spell Haste",
        haste,
        EntityAttributeModifier.Operation.MULTIPLY_BASE
    )
);


var critChance = 0.01; // +1% Spell Crit Chance
builder.put(SpellPowerMechanics.CRITICAL_CHANCE.attribute,
    new EntityAttributeModifier(
        SomeUUID,
        "Spell Crit Chance",
        critChance,
        EntityAttributeModifier.Operation.MULTIPLY_BASE
    )
);

var critDamage = 0.5; // +50% Spell Crit Damage
builder.put(SpellPowerMechanics.CRITICAL_DAMAGE.attribute,
    new EntityAttributeModifier(
        SomeUUID,
        "Crit Damage",
        critDamage,
        EntityAttributeModifier.Operation.MULTIPLY_BASE
    )
);
```

**How big bonuses should be used?**

As long as the user experience is intended to be Vanilla friendly, it is recommended to keep your Spell Power bonuses roughly in the same ballpark as vanilla `attack_damage` attribute. For example:
- A staff might have + 4 Fire Spell Power

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
        .setVulnerability(SpellSchools.FROST, new SpellPower.Vulnerability(0, 1F, 0F));

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

### Adopting Spell Haste

Spell Haste represents the casting speed of spells.

If implementing completely custom spells and want to calculate with Spell Haste attribute of players keep reading.

To retrieve the Spell Haste value of a player, use the following API:
```java
// Given `player` is a PlayerEntity
float haste = SpellPower.getHaste(player, SpellSchools.FIRE);
```

This value represents a relative casting speed. For example:
- When players have no haste bonus (so default attribute value) it returns `1.0`
- When players have 50% haste bonus (so attribute value of 150) it returns `1.5`

Haste can be calculated with using arbitrary formula. But the typical recommendation is the following:
```java
// Given `myCooldownDuration` is a valid number that presrents the duration of the cooldown
float hasteAffectedCooldownDuration = hasteAffectedValue(caster, myCooldownDuration);
// Given `mySpellCastDuration` is a valid number that presrents the duration of the spell cast
float hasteAffectedSpellCastDuration = hasteAffectedValue(caster, mySpellCastDuration);

float hasteAffectedValue(PlayerEntity caster, float value) {
    var haste = (float) SpellPower.getHaste(caster, SpellSchools.FIRE);
    return value / haste;
}
```

## Creating custom Spell Schools

To create a new spell school:
1. Create `SpellSchool` instance
2. Add its power sources (how damage, critical chance, critical damage, haste is calculated)
3. Register the school

In case you want an additional magic type, all of this can be done using the provided helper methods.

Example: Creating and registering "Blood" magic
```java
public class BloodMagicMod {
    // Creates school with the id of `spell_power:blood`, default namespace: `spell_power`
    public static final SpellSchool BLOOD = SpellSchools.createMagic("blood", 0x7e1c07);
}
``` 

For regular `magic` schools, it is strongly recommended to perform the registration with the following mixin, to ensure:
- Related attribute is generated and registered at the correct point
- Related status effect is generated and registered at the correct point
```java
@Mixin(value = SpellSchools.class)
public class SpellSchoolsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_BloodMagic(CallbackInfo ci) {
        SpellSchools.register(SpellSchools.createMagic("blood", 0x8B0000));
    }
}
```

Assets to add for new schools:
- Translation for school powering attribute (`"attribute.name.NAMESAPCE.MY_SCHOOL" : "My School Spell Power",`)
- Translation for school boosting status effect (`"effect.NAMESAPCE.MY_SCHOOL" : "My School Power"`)
- Icon for school boosting status effect (`textures/mob_effect/MY_SCHOOL.png`)

Note: translation for commonly occurring magic types are already included.