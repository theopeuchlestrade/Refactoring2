import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public String print(Invoice invoice, Map<String, Play> plays) {
    double totalAmount = 0;
    int volumeCredits = 0;

    // Header of the TXT with the customer name
    StringBuilder res = new StringBuilder();
    printTXT_Customer(res, invoice.customer);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      double thisAmount;

      // If the type of piece is "Unknown type"
      // then return an error
      if(play.type.equals("Unknown type"))
          throw new Error("unknown type: ${play.type}");
      // Else compute the amount of the piece
      else
        thisAmount = getThisAmount(play.type, perf.audience);

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);

      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this piece
      printTXT_ThisPiece(res, play.name, perf.audience, thisAmount);

      totalAmount += thisAmount;
    }
    // Total amount owed by this customer
    printTXT_TotalAmount(res, totalAmount);

    // Total credits earned by this customer
    printTXT_CreditsEarned(res, volumeCredits);

    return res.toString();
  }


  public String printHTML(Invoice invoice, Map<String, Play> plays) {
    double totalAmount = 0;
    int volumeCredits = 0;

    StringBuilder res = new StringBuilder();

    // Beginning of HTML file
    printHTML_beginning(res);

    // Print of the customer
    printHTML_Customer(res, invoice.customer);

    // Start the table of values
    printHTML_TableHeader(res);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      double thisAmount;

      // If the type of piece is "Unknown type"
      // then return an error
      if(play.type.equals("Unknown type"))
        throw new Error("unknown type: ${play.type}");
      // Else compute the amount for the piece
      else
        thisAmount = getThisAmount(play.type, perf.audience);

      // Add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);

      // Add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // Print line for this piece
      printHTML_ThisPiece(res, play.name, perf.audience, thisAmount);

      totalAmount += thisAmount;
    }
    // Total amount owed by this customer
    printHTML_TotalAmount(res, totalAmount);

    // Total credits earned by this customer
    printHTML_CreditsEarned(res, volumeCredits);

    // End of HTML table
    printHTML_TableEnd(res);

    // End of HTML file
    printHTML_end(res);

    return res.toString();
  }


  // Secondary Functions
  //----- ----- ----- ----- ----- ----- -----//
  //----- ----- ----- ----- ----- ----- -----//
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

  //All print used for the TXT string
  //----- ----- ----- ----- ----- ----- -----//

  public void printTXT_Customer(StringBuilder res, String customer){
    res.append("Statement for ");
    res.append(customer);
    res.append("\n");
  }
  public void printTXT_ThisPiece(StringBuilder res, String name, int audience, double amount){
    // Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);

    res.append("  ");
    res.append(name);
    res.append(": ");
    res.append(nbFormat.format(amount));
    res.append(" (");
    res.append(audience);
    res.append(" seats)\n");
  }

  public void printTXT_TotalAmount(StringBuilder res, double totalAmount){
    // Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);

    res.append("Amount owed is ");
    res.append(nbFormat.format(totalAmount));
    res.append("\n");
  }

  public void printTXT_CreditsEarned(StringBuilder res, int credits){
    res.append("You earned ");
    res.append(credits);
    res.append(" credits\n");
  }
  //----- ----- ----- ----- ----- ----- -----//


  //All print used for the HTML string
  //----- ----- ----- ----- ----- ----- -----//

  public void printHTML_beginning(StringBuilder res) {
    res.append("<!DOCTYPE html>");
    res.append("<html lang='fr'>");

    // Header of the HTML file
    res.append("<head>");
    // Adding style
    res.append("<style>");
    res.append("table, th, td {");
    res.append("border:1px solid black;");
    res.append(" border-collapse: collapse;");
    res.append("}");
    res.append("</style>");
    // Adding title to  HTML file
    res.append("<title>");
    res.append("Invoice");
    res.append("</title>");
    res.append("</head>");

    res.append("<body>");
  }
  public void printHTML_TableHeader(StringBuilder res){
    res.append("<table>");

    res.append("<tr>");
    res.append("<th>Piece</th>");
    res.append("<th>Seats Sold</th>");
    res.append("<th>Price</th>");
    res.append("</tr>");
  }
  public void printHTML_Customer(StringBuilder res, String customer){
    res.append("<h2>");
    res.append(customer);
    res.append("</h2>");
  }
  public void printHTML_ThisPiece(StringBuilder res, String name, int audience, double amount){
    // Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);

    res.append("<tr>");

    res.append("<td>");
    res.append(name);
    res.append("</td>");

    res.append("<td>");
    res.append(audience);
    res.append("</td>");

    res.append("<td>");
    res.append(nbFormat.format(amount));
    res.append("</td>");

    res.append("</tr>");

  }

  public void printHTML_TotalAmount(StringBuilder res, double totalAmount){
    // Transform the format of numbers from US to European and vice versa
    NumberFormat nbFormat = NumberFormat.getCurrencyInstance(Locale.US);

    res.append("<tr>");

    // Merging of the first two columns
    res.append("<td colspan='2'>");
    res.append("Total owed");
    res.append("</td>");

    res.append("<td>");
    res.append(nbFormat.format(totalAmount));
    res.append("</td>");

    res.append("</tr>");
  }

  public void printHTML_CreditsEarned(StringBuilder res, int credits){
    res.append("<tr>");

    // Merging of the first two columns
    res.append("<td colspan='2'>");
    res.append("Credits earned");
    res.append("</td>");

    res.append("<td>");
    res.append(credits);
    res.append("</td>");

    res.append("</tr>");
  }

  public void printHTML_TableEnd(StringBuilder res) {
    // End of HTML table
    res.append("</table>");
  }

  public void printHTML_end(StringBuilder res){
    // End of the document and HTML
    res.append("</body>");
    res.append("</html>");
  }

  //----- ----- ----- ----- ----- ----- -----//
  //----- ----- ----- ----- ----- ----- -----//
  //----- ----- ----- ----- ----- ----- -----//
}
