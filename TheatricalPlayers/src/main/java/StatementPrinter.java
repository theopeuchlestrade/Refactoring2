import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public String print(Invoice invoice, Map<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;

    //Header of the "string" with the customer name
    StringBuilder result = new StringBuilder( "Statement for ");
    result.append(invoice.customer);
    result.append("\n");

    //Transform the format of numbers from US to European and vice versa
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int thisAmount;

      // Use of different strategies depending on the type of the piece
      // To know the Amount it will need
      switch (play.type) {
        case "tragedy":
          thisAmount = 40000;
          if (perf.audience > 30) {
            thisAmount += 1000 * (perf.audience - 30);
          }
          break;
        case "comedy":
          thisAmount = 30000;
          if (perf.audience > 20) {
            thisAmount += 10000 + 500 * (perf.audience - 20);
          }
          thisAmount += 300 * perf.audience;
          break;
        // If the type of piece is not define or unknown
        // then return an error
        default:
          throw new Error("unknown type: ${play.type}");
      }

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this order
      result.append("  ");
      result.append(play.name);
      result.append(": ");
      result.append(frmt.format(thisAmount / 100));
      result.append(" (");
      result.append(perf.audience);
      result.append(" seats)\n");
      totalAmount += thisAmount;
    }
    // Total amount awed by this customer
    result.append("Amount owed is ");
    result.append(frmt.format(totalAmount / 100));
    result.append("\n");

    // Total credits earned by this customer
    result.append("You earned ");
    result.append(volumeCredits);
    result.append(" credits\n");
    return result.toString();
  }

}
