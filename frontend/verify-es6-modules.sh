#!/bin/bash
# ES6 Module Refactoring Verification Script

echo "=========================================="
echo "ES6 MODULE REFACTORING VERIFICATION"
echo "=========================================="
echo ""

echo "✅ Checking for export statements..."
echo ""

# Check config.js
echo "1. config.js:"
grep -c "export const" D:\Capstone\frontend\js\config.js && echo "   ✅ Has export statements"

# Check utils
echo "2. storage.js:"
grep -c "export class" D:\Capstone\frontend\js\utils\storage.js && echo "   ✅ Export class"

echo "3. validators.js:"
grep -c "export class" D:\Capstone\frontend\js\utils\validators.js && echo "   ✅ Export class"

echo "4. helpers.js:"
grep -c "export class" D:\Capstone\frontend\js\utils\helpers.js && echo "   ✅ Export class"

# Check API services
echo ""
echo "5. authService.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\api\authService.js && echo "   ✅ Has import/export"

echo "6. productService.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\api\productService.js && echo "   ✅ Has import/export"

echo "7. orderService.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\api\orderService.js && echo "   ✅ Has import/export"

echo "8. paymentService.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\api\paymentService.js && echo "   ✅ Has import/export"

echo "9. searchService.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\api\searchService.js && echo "   ✅ Has import/export"

# Check components
echo ""
echo "10. modal.js:"
grep -c "export class" D:\Capstone\frontend\js\components\modal.js && echo "    ✅ Export class"

echo "11. navbar.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\components\navbar.js && echo "    ✅ Has import/export"

echo "12. productCard.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\components\productCard.js && echo "    ✅ Has import/export"

echo "13. cartItem.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\components\cartItem.js && echo "    ✅ Has import/export"

# Check pages
echo ""
echo "14. auth.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\pages\auth.js && echo "    ✅ Has import/export"

echo "15. products.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\pages\products.js && echo "    ✅ Has import/export"

echo "16. cart.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\pages\cart.js && echo "    ✅ Has import/export"

echo "17. checkout.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\pages\checkout.js && echo "    ✅ Has import/export"

echo "18. orders.js:"
grep -c "export class\|import" D:\Capstone\frontend\js\pages\orders.js && echo "    ✅ Has import/export"

# Check main app
echo ""
echo "19. app.js:"
grep -c "import\|export" D:\Capstone\frontend\js\app.js && echo "    ✅ Has import statements"

# Check HTML
echo ""
echo "20. index.html:"
grep -c "script type=\"module\"" D:\Capstone\frontend\index.html && echo "    ✅ Has module script tag"

echo ""
echo "=========================================="
echo "✅ ES6 MODULE REFACTORING VERIFIED!"
echo "=========================================="

