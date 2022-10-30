import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public String print(Invoice invoice, Map<String, Play> plays) {
    double totalAmount = 0;
    int volumeCredits = 0;

    //Header of the "string" with the customer name
    StringBuilder res = new StringBuilder( "Statement for ");
    res.append(invoice.customer);
    res.append("\n");

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      double thisAmount;

      // If the type of piece is "Unknown type"
      // then return an error
      if(play.type.equals("Unknown type"))
          throw new Error("unknown type: ${play.type}");
      // Compute the amount of the piece
      else
        thisAmount = getThisAmount(play.type, perf.audience);

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this piece
      printThisPiece(res, play.name, perf.audience, thisAmount);

      totalAmount += thisAmount;
    }
    // Total amount owed by this customer
    printTotalAmount(res, totalAmount);

    // Total credits earned by this customer
    printCreditsEarned(res, volumeCredits);
    return res.toString();
  }

  public double getThisAmount(String type, int audience){
    double theAmount;

    // Use of different strategies depending on the type of the piece
    // To know the Amount it will need
    switch (type) {
      case "tragedy":
        theAmount = getTragedyAmount(audience);
        break;
      case "comedy":
        theAmount = getComedyAmount(audience);
        break;

        // Goes here when the type exist but not define here
      default:
        throw new Error("Type exists but not define !");
    }

    return theAmount;
  }

  //Specific functions for each type of pieces
  //----- ----- ----- ----- ----- ----- -----//
  public double getComedyAmount(int audience){
    double amount = 300.00;

    if (audience > 20) {
      amount += 100.00 + 5.00 * (audience - 20);
    }
    amount += 3.00 * audience;

    return amount;
  }

  public double getTragedyAmount(int audience){
    double amount = 400.00;

    if (audience > 30) {
      amount += 10.00 * (audience - 30);
    }

    return amount;
  }
  //----- ----- ----- ----- ----- ----- -----//

  //Different type of print used for the string
  //----- ----- ----- ----- ----- ----- -----//
  public void printThisPiece(StringBuilder res, String name, int audience, double amount){
    //Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);

    res.append("  ");
    res.append(name);
    res.append(": ");
    res.append(nbFormat.format(amount));
    res.append(" (");
    res.append(audience);
    res.append(" seats)\n");
  }

  public void printTotalAmount(StringBuilder res, double totalAmount){
    //Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);
    
    res.append("Amount owed is ");
    res.append(nbFormat.format(totalAmount));
    res.append("\n");
  }
  
  public void printCreditsEarned(StringBuilder res, int credits){
    res.append("You earned ");
    res.append(credits);
    res.append(" credits\n");
  }

  //----- ----- ----- ----- ----- ----- -----//
}
