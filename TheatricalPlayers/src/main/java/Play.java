public class Play {

  public String name;
  public String type;

  public enum Types{
    tragedy, comedy
  }

  public Play(String name, String type) {
    this.name = name;
    this.type = typeExist(type);
  }

  private String typeExist(String value)
  {
    for (Types pieceType : Types.values())
    {
      if (pieceType.name().equals(value))
      {
        return value;
      }
    }

    return "Unknown type";
  }
}
