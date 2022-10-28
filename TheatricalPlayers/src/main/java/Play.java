public class Play {

  public String name;
  public String type;

  List<String> validTypes = new ArrayList<>(List.of("tragedy", "comedy"));

  public Play(String name, String type) {
    this.name = name;
    this.type = validate(type);
  }

  private String validate(String type) {
    if(validTypes.contains(type)){
      return type;
    }
    else{
      return "Unknown type";
    }
  }
}
