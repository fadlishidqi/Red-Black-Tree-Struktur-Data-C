public class RedBlackTree {
    static class Node {
        int data;    // nilai data.
        Node left;   // menunjuk ke anak kiri
        Node right;  // menunjuk ke anak kanan.
        Node parent; // menunjuk ke node induk.
        char color;  // warna node.

        public Node(int data, char color) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.color = color;
        }
    }

    static Node root;  // akar pohon.

    // fungsi untuk membuat Node.
    public static Node buatNode(int data) {
        Node node = new Node(data, 'R');
        node.left = new Node(-1, 'B');
        node.right = new Node(-1, 'B');
        return node;
    }

    // fungsi insert ke dalam pohon.
    public static void tambah(int data) {
        System.out.println("Menyisipkan data: " + data);
        Node node = buatNode(data);
        if (root == null) {
            root = node;
            root.color = 'B';
            return;
        }

        Node temp = root;
        while (temp != null) {
            if (temp.data > data) {
                if (temp.left.data == -1) {
                    temp.left = node;
                    node.parent = temp;
                    seimbangkan(node);               // seimbangkan pohon.
                    return;
                }
                temp = temp.left;
                continue;
            }
            if (temp.data < data) {
                if (temp.right.data == -1) {
                    temp.right = node;
                    node.parent = temp;
                    seimbangkan(node);               // seimbangkan pohon.
                    return;
                }
                temp = temp.right;
            }
        }
    }

    // fungsi untuk menghapus elemen dari pohon.
    public static void hapus(int data) {
        System.out.println("Menghapus data: " + data);
        if (root == null) {
            return;
        }

        // cari elemen yang diberikan dalam pohon.
        Node temp = root;
        while (temp.data != -1) {
            if (temp.data == data) {
                break;
            }
            if (temp.data > data) {
                temp = temp.left;
                continue;
            }
            if (temp.data < data) {
                temp = temp.right;
                continue;
            }
        }

        // jika tidak ditemukan. maka kembalikan.
        if (temp.data == -1) {
            return;
        }

        Node next = cariBerikutnya(temp);
        int t = temp.data;
        temp.data = next.data;
        next.data = t;

        // hapus node berikutnya.
        Node parent = next.parent;
        if (parent == null) {
            if (next.left.data == -1) {
                root = null;
            } else {
                root = next.left;
                next.parent = null;
                root.color = 'B';
            }
            return;
        }

        if (parent.right == next) {
            parent.right = next.left;
        } else {
            parent.left = next.left;
        }
        next.left.parent = parent;
        String color = Character.toString(next.left.color) + Character.toString(next.color);
        seimbangkan(next.left, color);  // seimbangkan pohon.
    }

    // fungsi untuk menyeimbangkan pohon SAAT PENGHAPUSAN.
    private static void seimbangkan(Node node, String color) {
        System.out.println("Menyeimbangkan Node: " + node.data + " Warna: " + color);

        // jika node adalah Red-Black.
        if (node.parent == null || color.equals("BR") || color.equals("RB")) {
            node.color = 'B';
            return;
        }

        Node parent = node.parent;

        // dapatkan node sibling dari node yang diberikan.
        Node sibling = null;
        if (parent.left == node) {
            sibling = parent.right;
        } else {
            sibling = parent.left;
        }

        Node sibleft = sibling.left;   // node kiri saudara.
        Node sibright = sibling.right; // node kanan saudara.

        // sibling, sibleft, dan sibright semuanya hitam.
        if (sibling.color == 'B' && sibleft.color == 'B' && sibright.color == 'B') {
            sibling.color = 'R';
            node.color = 'B';
            String c = Character.toString(parent.color) + Character.toString('B');
            seimbangkan(parent, c);
            return;
        }

        // sibling merah.
        if (sibling.color == 'R') {
            if (parent.right == sibling) {
                rotasiKiri(sibling);
            } else {
                rotasiKanan(sibling);
            }
            seimbangkan(node, color);
            return;
        }

        // sibling merah tetapi salah satu dari anaknya merah.
        if (parent.left == sibling) {
            if (sibleft.color == 'R') {
                rotasiKanan(sibling);
                sibleft.color = 'B';
            } else {
                rotasiKiri(sibright);
                rotasiKanan(sibright);
                sibright.left.color = 'B';
            }
            return;
        } else {
            if (sibright.color == 'R') {
                rotasiKiri(sibling);
                sibright.color = 'B';
            } else {
                rotasiKanan(sibleft);
                rotasiKiri(sibleft);
                sibleft.right.color = 'B';
            }
            return;
        }
    }

    // fungsi untuk menemukan angka yang lebih besar berikutnya dari node yang diberikan.
    private static Node cariBerikutnya(Node node) {
        Node next = node.right;
        if (next.data == -1) {
            return node;
        }
        while (next.left.data != -1) {
            next = next.left;
        }
        return next;
    }

    // fungsi untuk menyeimbangkan pohon SAAT PENYISIPAN.
    public static void seimbangkan(Node node) {
        System.out.println("Menyeimbangkan Node : " + node.data);

        // jika node yang diberikan adalah root node.
        if (node.parent == null) {
            root = node;
            root.color = 'B';
            return;
        }

        // jika warna induk node adalah hitam.
        if (node.parent.color == 'B') {
            return;
        }

        // dapatkan node sibling induk node.
        Node sibling = null;
        if (node.parent.parent.left == node.parent) {
            sibling = node.parent.parent.right;
        } else {
            sibling = node.parent.parent.left;
        }

        // jika warna sibling adalah merah.
        if (sibling.color == 'R') {
            node.parent.color = 'B';
            sibling.color = 'B';
            node.parent.parent.color = 'R';
            seimbangkan(node.parent.parent);
            return;
        }

        // jika warna sibling adalah hitam.
        else {
            if (node.parent.left == node && node.parent.parent.left == node.parent) {
                rotasiKanan(node.parent);
                seimbangkan(node.parent);
                return;
            }
            if (node.parent.right == node && node.parent.parent.right == node.parent) {
                rotasiKiri(node.parent);
                seimbangkan(node.parent);
                return;
            }
            if (node.parent.right == node && node.parent.parent.left == node.parent) {
                rotasiKiri(node);
                rotasiKanan(node);
                seimbangkan(node);
                return;
            }
            if (node.parent.left == node && node.parent.parent.right == node.parent) {
                rotasiKanan(node);
                rotasiKiri(node);
                seimbangkan(node);
                return;
            }
        }
    }

    // fungsi untuk melakukan Rotasi Kiri.
    private static void rotasiKiri(Node node) {
        System.out.println("Melakukan rotasi kiri  : " + node.data);
        Node parent = node.parent;
        Node left = node.left;
        node.left = parent;
        parent.right = left;
        if (left != null) {
            left.parent = parent;
        }
        char c = parent.color;
        parent.color = node.color;
        node.color = c;
        Node gp = parent.parent;
        parent.parent = node;
        node.parent = gp;

        if (gp == null) {
            root = node;
            return;
        } else {
            if (gp.left == parent) {
                gp.left = node;
            } else {
                gp.right = node;
            }
        }
    }

    // fungsi untuk melakukan Rotasi Kanan.
    private static void rotasiKanan(Node node) {
        System.out.println("Melakukan rotasi kanan : " + node.data);
        Node parent = node.parent;
        Node right = node.right;
        node.right = parent;
        parent.left = right;
        if (right != null) {
            right.parent = parent;
        }
        char c = parent.color;
        parent.color = node.color;
        node.color = c;
        Node gp = parent.parent;
        parent.parent = node;
        node.parent = gp;

        if (gp == null) {
            root = node;
            return;
        } else {
            if (gp.left == parent) {
                gp.left = node;
            } else {
                gp.right = node;
            }
        }
    }

    // fungsi untuk Traversal PreOrder dari pohon.
    private static void preOrder(Node node) {
        if (node.data == -1) {
            return;
        }
        System.out.print(node.data + "-" + node.color + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    // fungsi untuk menampilkan pohon.
    public static void tampil() {
        if (root == null) {
            System.out.println("Pohon Kosong");
            return;
        }

        System.out.print("PreOrder Traversal Pohon: ");
        preOrder(root);
        System.out.println();
    }

    // fungsi utama untuk menjalankan program.
    public static void main(String[] args) {
        tambah(10);
        tampil();

        tambah(18);
        tampil();

        tambah(7);
        tampil();

        tambah(15);
        tampil();

        tambah(16);
        tampil();

        tambah(30);
        tampil();

        tambah(25);
        tampil();

        tambah(40);
        tampil();

        hapus(30);
        tampil();
    }
}
