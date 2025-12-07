# 1.4.6

- Enchantment that require matching attributes can now be configured to disable this requirement
- Fix unsafe check in enchantment logic
- Update translations

# 1.4.5

- Update translations

# 1.4.4

- Spell Volatility enchantment is now available for Weapons only
- Spell Volatility and Amplify Spell enchantments are now mutually exclusive
- Enchantment descriptions now have a trailing dot, to be consistent with vanilla enchantments
- Add effect descriptions support
- Add innate spell power bonus

# 1.4.3

- Improve thread safety (for NeoForge)
- Retire attribute registration scope config, now everything is registered for LivingEntity
- Update TinyConfig

# 1.4.2

- Spell Power damage types now cause panic for mobs

# 1.4.1

- Fix innate attribute modifiers for spell crit chance, and spell crit damage attributes (now correctly show up when queried, or on GUI)

# 1.4.0

- Migrate to Architectury
- Rework generic Spell Power attribute implementation, to be multiplier attribute

# 1.3.1

- Resistance attribute is now configurable
  - resistance_curve: `LINEAR`, `QUADRATIC`, `HYPERBOLIC`
  - resistance_tuning_constant: the constant used in resistance formula
  - new default is `HYPERBOLIC` with `20` constant

# 1.3.0

- Rework Amplify Spell 
  - grants 5x10% spell critical damage
  - applies to weapons
- Fix effect descriptions compatibility

# 1.2.4

- Add spell power enchantable item tags to java API

# 1.2.3

- Improve spell resistance attribute calculation

# 1.2.2

- Update chinese translations

# 1.2.1

- Nerf spell critical strike related enchantments

# 1.2.0

- Expose `isCritical` data on SpellPower.Result queries

# 1.1.2

- Attempt fix cross powered attribute crash #37

# 1.1.1

- Add exposed potion registration function
- Update potion ids and translation keys

# 1.1.0

- Add spell potions, disabled by default (can be turned on `config/spell_power/attributes.json`)

# 1.0.8

- Magic Protection enchantment now uses `c:is_magic` for compatibility with other mods

# 1.0.7

- Add all custom spell power related damage types to `c:is_magic`, for improved compatibility with other mods

# 1.0.6

- Add Russian translation, thanks to @Heimdallr
- Add Brazilian translation, thanks to @demorogabrtz

# 1.0.5

- Fix spell vulnerability status effects

# 1.0.4

- Lower Fabric API version requirement

# 1.0.3

- Fix cross-functional attribute calculations (reverse updates)
- Update dependency declarations

# 1.0.2

- Restore school filtering for specialised enchantments
- School specific enchantments are now exclusive to each other

# 1.0.1

- Update to Minecraft 1.21.1

# 1.0.0

- Update to Minecraft 1.21.0

# 0.12.0

- Add school specific damage types
- Add generic spell resistance attribute

# 0.11.1

- Attempt to fix SpellSchool serialization

# 0.11.0

- Rework enchantment conditioning, now based on item tags `spell_power:`
  - `enchant_critical_damage`
  - `enchant_critical_chance`
  - `enchant_haste`
  - `enchant_spell_power_energize`
  - `enchant_spell_power_generic`
  - `enchant_spell_power_soulfrost`
  - `enchant_spell_power_specialized`
  - `enchant_spell_power_sunfire`

# 0.10.3 

- Update Fabric Loader to 15+ for embedded MixinExtras

# 0.10.2

API Changes:
- BREAKING! - Replaced several crucial types, most notably: MagicSchool -> SpellSchool
- BREAKING! - Removed PHYSICAL_RANGED and PHYSICAL_MELEE schools
- SpellSchools can now be added from external code
- SpellSchool specifically, various trait calculation sources can now be added via API (for example: custom power/haste/crit sources)
- EntityAttribute registration now happens at vanilla Attribute registry init, so custom AttributeResolver components are no longer neccesary to resolve attriubtes by ID
- Optimized Spell Power related attribute container injections, (now it is possible to configure only PlayerEntity to get them, instead of LivingEntity)

Functional changes:
- Update physical ranged school color
- Improve the way how disabled Enchantments are handled
- Fix enchantment config type requirement not working correctly for some cases

# 0.9.19

- Add partial mutex for spell enchantments and ranged weapon enchantments

# 0.9.18

- Add `PHYSICAL_RANGED` school

# 0.9.17

- Fix damage type tags

# 0.9.16

- SpellDamageSource now returns DamageSource with vanilla damage type ("minecraft:magic")  (Can be disabled in attributes config)

# 0.9.15

- Update Arcane Power icon
- Unique damage type identifier for each magic school
- Disable fire spell damage counting is actual fire damage (can be restored using data pack)

# 0.9.14

- Add italian translation thanks to Zano1999 #11

# 0.9.13

- Update to 1.20.1
- Data based custom damage sources

# 0.9.12

- Refactor custom enchantment conditioning API 

# 0.9.11

- Rebalance default bonus values for enchantments

# 0.9.10

- Fix enchantments not stacking 

# 0.9.9

- Enable Energize

# 0.9.8

- Add support for physical spell damage (aka ability damage)
- Add config versioning, so old configs can be overridden
- Add Ukrainian translation, thanks to un_roman
- Remove deprecated interfaces
- Fix some issues for turkish players

# 0.9.7
- Update enchanting API
- Add magic school to SpellDamageSource

# 0.9.6
- Reduce default bonus per stack of spell power status effects (to rebalance Arcane spec)

# 0.9.5
- Magic schools specific enchantments will be no longer applied to irrelevant pieces (such as no Sunfire for frost items)

# 0.9.4
- Update license to LGPL v3
- Add chinese translation by Kzeroko and Kasualix
- Rework Spell Attributes API
- Improve status effect configuration structure

# 0.9.3
- Update API for named attributes
- Update enchantment config defaults

# 0.9.1
- Update query interface
- Fixate rawId for status effects

# 0.9.0
- Initial release

# -