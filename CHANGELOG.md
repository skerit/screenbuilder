## 0.4.2 (WIP)

* Upgrade to 1.20.4

## 0.4.1 (2023-07-07)

* Add `InventoryProxy` class
* Improve error area handling
* Fix certain positioning issues

## 0.4.0

* Upgrade to 1.20

## 0.3.1 (2023-05-26)

* Upgrade to 1.19.4
* Get the correct title when using a ScreenBuilder as a screen factory
* Support newlines when setting lore on a `ButtonWidgetSlot`
* Allow setting a ScreenBuilder's display name with just a string
* Add generics to the Value class
* Add `Value#setOnChange(Runnable)` method

## 0.3.0 (2023-03-11)

* Upgrade to 1.19.3
* Add support for scaled icons
* Add new select input
* Add file selector input

## 0.2.1 (2022-12-27)

* Add icons
* Allow textures to be recoloured
* Try to re-use the current syncid when showing a new screen
* Fix `TexturedScreenHandler#transferSlot()` causing infinite loop
* Fix `Widget`s not always getting the parent `ScreenBuilder` instance set
* Allow `Widget`s to register themselves
* Fix `TextureWidget`s adding themselves to a `TextBuilder` multiple times
* Fix render issues by using at least 1 splitting character
* Add the `space` as a hardcoded 4px-wide whitespace character
* Improve getting a slot's coordinates
* Make ProgressWidget register itself correctly
* Allow adding "overlays" to `BaseSlot` instances
* Improve the showing of error messages