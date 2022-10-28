import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public String print(Invoice invoice, Map<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;

    //Header of the "string" with the customer name
    StringBuilder res = new StringBuilder( "Statement for ");
    res.append(invoice.customer);
    res.append("\n");

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
        // If the type of piece is "Unknown type"
        // then return an error
        case "Unknown type":
          throw new Error("unknown type: ${play.type}");
        default:
          throw new Error("WTF is happening ! Why are we here !! Oh no no nooooo");
      }

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this order
      res.append("  ");
      res.append(play.name);
      res.append(": ");
      res.append(frmt.format(thisAmount / 100));
      res.append(" (");
      res.append(perf.audience);
      res.append(" seats)\n");
      totalAmount += thisAmount;
    }
    // Total amount awed by this customer
    res.append("Amount owed is ");
    res.append(frmt.format(totalAmount / 100));
    res.append("\n");

    // Total credits earned by this customer
    res.append("You earned ");
    res.append(volumeCredits);
    res.append(" credits\n");
    return res.toString();
  }

}
