/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatting;

/**
 *
 * @author Frost
 */
public class inputFormatting {
  public inputFormatting() {}
  public String capitalizeWords(String str) {
    String[] words = str.split("\\s");
    StringBuilder sb = new StringBuilder();

    for (String word: words) {
      if (!word.isEmpty()) {
        String capitalizedWord = Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
        sb.append(capitalizedWord).append(" ");
      }
    }

    return sb.toString().trim();
  }
  
  
}