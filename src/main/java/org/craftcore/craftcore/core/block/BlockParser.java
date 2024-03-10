package org.craftcore.craftcore.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public class BlockParser {
  public static BlockState parseBlockState(String input) throws IllegalArgumentException {
    // Teilt den Eingabestring in Basisblock-ID und Eigenschaften
    String[] parts = input.split("\\[", 2);
    String idPart = parts[0];
    String propertiesPart = parts.length > 1 ? parts[1].replaceAll("]", "") : "";

    // Erstellt einen Identifier und holt den Block aus dem Registry
    Identifier id = new Identifier(idPart);
    Block block = Registries.BLOCK.get(id);
    BlockState blockState = block.getDefaultState();

    if (!propertiesPart.isEmpty()) {
      // Verarbeitet jede Eigenschaft
      String[] propertiesArray = propertiesPart.split(",");
      for (String propertyString : propertiesArray) {
        String[] keyValue = propertyString.split("=");
        String key = keyValue[0];
        String value = keyValue[1];

        // Sucht die Eigenschaft im BlockState
        Property<?> property =
            blockState.getProperties().stream()
                .filter(p -> p.getName().equals(key))
                .findFirst()
                .orElseThrow(
                    () -> new IllegalArgumentException("Eigenschaft nicht gefunden: " + key));

        // Setzt die Eigenschaft im BlockState, wenn der Wert gültig ist
        blockState = setValue(blockState, property, value);
      }
    }

    return blockState;
  }

  private static <T extends Comparable<T>> BlockState setValue(
      BlockState state, Property<T> property, String valueString) {
    // Findet den passenden Wert für die Eigenschaft und setzt diesen, wenn möglich
    return property
        .parse(valueString)
        .map(value -> state.with(property, value))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Ungültiger Wert für Eigenschaft: "
                        + property.getName()
                        + " Wert: "
                        + valueString));
  }
}
