import java.util.Objects;

public class Vector {
  public int r;
  public int c;

  public Vector(int r, int c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public int hashCode() {
    int result = 13;
    result = 31 * result + r;
    result = 31 * result + c;

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Vector)) {
      return false;
    }

    Vector v = (Vector) obj;
    return r == v.r && c == v.c;
  }
}
