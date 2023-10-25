public class avlTree1 {

	public static class TreeNode<Element> {

		private Element value;
		private int height;
		private TreeNode<Element> left;
		private TreeNode<Element> right;

		public TreeNode(Element value) { 
			this.value = value;
			this.left = null;
			this.right = null;
		}
	}

	public static class AVLTree<Element> {
		private TreeNode<Element> root;

		public void updateHeight(TreeNode<Element> node) { node.height = 1 + Math.max(height(node.left), height(node.right)); }
		public int height(TreeNode<Element> node) { return (node == null) ? -1 : node.height; }
		public int getBalance(TreeNode<Element> node) { return (node == null) ? 0 : height(node.right) - height(node.left); }
		public TreeNode<Element> mostLeftChild(TreeNode<Element> node) { return (node.left == null) ? node : mostLeftChild(node.left) ;}

		/**
		 * Compares two double-casted values of user-specified element type and comparison type.
		 * <br><br>
		 * TODO: FIX THIS. DOUBLE CAN'T CAST OTHER TYPES, MUST FIND A WAY TO COMPARE ARBITRARY, TYPE-UNKNOWN-AT-COMPILE-TIME OBJECTS/ELEMENTS.
		 * <br><br>
		 * Each parameter passed is casted to type <code>Double</code> for the comparison
		 * from user-specified type <code>Element</code>
		 * @param val1	The value being compared (left operand: <u><b>x</b></u> < y)
		 * @param val2	The value to compare to (right operand: x < <u><b>y</b></u>)
		 * @param op	An <b><code>int</code></b> value specifying the type of comparison to make:<br>
		 * 				&emsp;&emsp;0: <code>"<"</code>&emsp;,&emsp;1: <code>"<="</code>
		 * 				&emsp;,&emsp;2: <code>">"</code>&emsp;,&emsp;3: <code>">="</code>
		 * 				&emsp;,&emsp;4: <code>"=="</code>
		 * @return		<code>true</code> if the comparison evaluates as such; <code>false</code> otherwise,
		 * 				or if an invalid <code>int</code> specifying the comparison operation is passed
		 * 				(0 <= <code>op</code> <= 4)
		 */
		public boolean cmp(Element val1, Element val2, int op) {
			switch (op) {
			case 0:		// <
				return ((Double) val1 < (Double) val2);
			case 1:		// <=
				return ((Double) val1 <= (Double) val2);
			case 2:		// >
				return ((Double) val1 > (Double) val2);
			case 3:		// >=
				return ((Double) val1 >= (Double) val2);
			case 4:		// ==
				return (val1 == val2);
			default:
				return false;
			}
		}

		public TreeNode<Element> rotateRight(TreeNode<Element> y) {
			TreeNode<Element> x = y.left;
			TreeNode<Element> z = x.right;
			x.right = y;
			y.left = z;
			updateHeight(y);
			updateHeight(x);
			return x;
		}

		public TreeNode<Element> rotateLeft(TreeNode<Element> y) {
			TreeNode<Element> x = y.right;
			TreeNode<Element> z = x.left;
			x.left = y;
			y.right = z;
			updateHeight(y);
			updateHeight(x);
			return x;
		}

		public TreeNode<Element> rebalance(TreeNode<Element> z) {
			updateHeight(z);
			int balance = getBalance(z);
			if (balance > 1) {
				if (height(z.right.right) > height(z.right.left)) {
					z = rotateLeft(z);
				} else {
					z.right = rotateRight(z.right);
					z = rotateLeft(z);
				}
			} else if (balance < -1) {
				if (height(z.left.left) > height(z.left.right))
					z = rotateRight(z);
				else {
					z.left = rotateLeft(z.left);
					z = rotateRight(z);
				}
			}
			return z;
		}

		public void delete(Element value) { root = delNode(root, value); }

		public TreeNode<Element> delNode(TreeNode<Element> node, Element value) {
			if (node == null) {
				return node;
			} else if (cmp(node.value, value, 2)) {
				node.left = delNode(node.left, value);
			} else if (cmp(node.value, value, 0)) {
				node.right = delNode(node.right, value);
			} else {
				if (node.left == null || node.right == null) {
					node = (node.left == null) ? node.right : node.left;
				} else {
					TreeNode<Element> mostLeftChild = mostLeftChild(node.right);
					node.value = mostLeftChild.value;
					node.right = delNode(node.right, node.value);
				}
			}
			if (node != null) {
				node = rebalance(node);
			}
			return node;
		}

		public TreeNode<Element> find(Element value) {
			TreeNode<Element> current = root;
			while (current != null) {
				if (current.value == value) break; 
				current = (cmp(current.value, value, 0)) ? current.right : current.left;
			}
			return current;
		}

		public void insert(Element value) { root = insert(root, value); }

		private TreeNode<Element> insert(TreeNode<Element> node, Element value) {
			if (node == null) { return new TreeNode<>(value); } 
			if (cmp(value, node.value, 0)) { node.left = insert(node.left, value); }
			else if (cmp(value, node.value, 2)) { node.right = insert(node.right, value); }
			else { return node; }
			updateHeight(node);
			return rebalance(node);
		}

		public void printInOrder() { inOrderTraversalPrint(root); }

		private void inOrderTraversalPrint(TreeNode<Element> node) {
			if (node != null) {
				inOrderTraversalPrint(node.left);
				System.out.println(" " + node.value);
				inOrderTraversalPrint(node.right);
			}
		}

		public void printChildren() { childPrinter(root, "\nRoot: "); }

		private void childPrinter(TreeNode<Element> node, String str) {
			if (node == null) return;
			System.out.println(str + node.value);
			childPrinter(node.left, "Left of " + node.value + ": ");
			childPrinter(node.right, "Right of " + node.value + ": ");
		}

	}

	public static void main(String[] args) {

		AVLTree<Double> tree1 = new AVLTree<>();

		tree1.insert(10d);
		tree1.insert(20d);
		tree1.insert(30d);
		tree1.insert(40d);
		tree1.insert(50d);

		tree1.printInOrder();
		tree1.printChildren();
		System.out.println("\n~~~Print 1 done...~~~\n");

		tree1.insert(9d);
		tree1.insert(8d);
		tree1.insert(7d);
		tree1.insert(6d);

		tree1.printInOrder();
		tree1.printChildren();
		System.out.println("\n~~~Print 2 done...~~~\n");


		tree1.insert(51d);
		tree1.insert(52d);
		tree1.insert(53d);

		tree1.printInOrder();
		tree1.printChildren();
		System.out.println("\n~~~Print 3 done...~~~\n");

		tree1.delete(10d);
		tree1.delete(30d);
		tree1.delete(6d);
		tree1.delete(7d);

		tree1.printInOrder();
		tree1.printChildren();
		System.out.println("\n~~~Print 4 done...~~~\n");
	}
}